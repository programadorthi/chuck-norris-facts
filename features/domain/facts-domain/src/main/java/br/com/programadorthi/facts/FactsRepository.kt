package br.com.programadorthi.facts

import io.reactivex.Single

interface FactsRepository {

    fun fetchCategories(): Single<List<String>>

    fun doSearch(text: String): Single<List<Fact>>
}
