package br.com.programadorthi.facts

import io.reactivex.Single

interface FactsUseCase {

    fun categories(offset: Int): Single<List<String>>

    fun search(text: String): Single<List<Fact>>
}
