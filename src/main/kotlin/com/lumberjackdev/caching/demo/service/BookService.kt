package com.lumberjackdev.caching.demo.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class BookService {
    private val books: Map<String, List<String>> = mapOf(
            "Brandon Sanderson" to listOf("The Way of Kings", "Words of Radiance", "Oathbringer"),
            "Steven Erikson" to listOf("Gardens of the Moon", "Dust of Dreams", "Deadhouse Gates"),
            "Ian C. Esslemont" to listOf("Night of Knives", "Return of the Crimson Guard"))

    @Cacheable(cacheNames = ["books"], cacheManager = "jCacheCacheManager")
    fun getBooks(author: String) = books.getOrDefault(author, emptyList())

    @Cacheable(cacheNames = ["authors"], key = "'all-authors'")
    fun getAuthors() = listOf(books.keys.toTypedArray())
}