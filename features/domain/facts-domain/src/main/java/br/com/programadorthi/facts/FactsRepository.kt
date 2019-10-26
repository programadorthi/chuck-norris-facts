package br.com.programadorthi.facts

import io.reactivex.Single

interface FactsRepository {

    fun fetchCategories(limit: Int, shuffle: Boolean): Single<List<String>>

    fun getLastSearches(): Single<List<String>>

    fun doSearch(text: String): Single<List<Fact>>
}
