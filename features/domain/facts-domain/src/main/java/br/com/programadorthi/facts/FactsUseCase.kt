package br.com.programadorthi.facts

import io.reactivex.Single

interface FactsUseCase {

    fun categories(offset: Int, shuffle: Boolean): Single<List<String>>

    fun lastSearches(): Single<List<String>>

    fun search(text: String): Single<List<Fact>>
}
