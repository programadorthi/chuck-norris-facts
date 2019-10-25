package br.com.programadorthi.facts

import io.reactivex.Single

interface FactsRepository {

    fun fetchCategories(offset: Int, shuffle: Boolean): Single<List<String>>

    fun getLastSearches(): Single<List<String>>

    fun doSearch(text: String): Single<List<Fact>>
}
