package br.com.programadorthi.facts.domain

import io.reactivex.Single

interface FactsUseCase {

    fun categories(limit: Int, shuffle: Boolean): Single<List<String>>

    fun lastSearches(): Single<List<String>>

    fun search(text: String): Single<List<Fact>>
}
