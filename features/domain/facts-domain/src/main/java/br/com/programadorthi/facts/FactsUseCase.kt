package br.com.programadorthi.facts

import io.reactivex.Single

interface FactsUseCase {

    fun categories(): Single<List<String>>

    fun search(text: String): Single<List<Fact>>
}
