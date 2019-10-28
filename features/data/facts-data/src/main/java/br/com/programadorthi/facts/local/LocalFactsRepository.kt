package br.com.programadorthi.facts.local

import io.reactivex.Single

interface LocalFactsRepository {

    fun getCategories(): Single<List<String>>

    fun getLastSearches(): Single<List<String>>

    fun saveCategories(categories: List<String>)

    fun saveNewSearch(text: String)
}
