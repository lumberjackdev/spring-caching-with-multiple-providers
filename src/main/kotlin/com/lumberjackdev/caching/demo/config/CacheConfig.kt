package com.lumberjackdev.caching.demo.config

import org.springframework.beans.factory.BeanClassLoaderAware
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import javax.cache.Caching


@Configuration
@EnableCaching
class CacheConfig(
        val redisConnectionFactory: RedisConnectionFactory,
        val jCacheManagerCustomizers: ObjectProvider<JCacheManagerCustomizer>) : CachingConfigurerSupport(), BeanClassLoaderAware {

    lateinit var classLoader: ClassLoader

    @Bean
    @Primary
    override fun cacheManager(): CacheManager =
            RedisCacheManager.builder(redisConnectionFactory).build()

    @Bean
    fun jCacheCacheManager(): JCacheCacheManager {
        val cachingProvider = Caching.getCachingProvider()
        val configLocation = cacheProperties().resolveConfigLocation(cacheProperties().jcache.config)
        val cacheManager = cachingProvider.getCacheManager(configLocation.uri, classLoader)
        jCacheManagerCustomizers.orderedStream().forEach { it.customize(cacheManager) }
        return JCacheCacheManager(cacheManager)
    }

    @Bean
    fun cacheProperties() = CacheProperties()

    override fun setBeanClassLoader(classLoader: ClassLoader) {
        this.classLoader = classLoader
    }
}