package com.lumberjackdev.caching.demo.service

import com.lumberjackdev.caching.demo.DemoApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [DemoApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookServiceTests {

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var jcacheManager: JCacheCacheManager

    @Test
    fun `fetching books for an author should be cached`() {
        bookService.getBooks("Steven Erikson")

        assertThat(jcacheManager.cacheManager!!.getCache<String, List<String>>("books").containsKey("Steven Erikson")).isTrue()
    }
}