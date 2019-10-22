package br.com.programadorthi.facts.local

import io.reactivex.Completable
import io.reactivex.Single

interface LocalFactsRepository {

    fun getCategories(): Single<List<String>>

    fun getLastSearches(): Single<List<String>>

    fun saveCategories(categories: List<String>): Completable

    fun saveNewSearch(text: String): Completable
}
