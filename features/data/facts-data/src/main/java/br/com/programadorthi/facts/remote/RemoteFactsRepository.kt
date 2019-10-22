package br.com.programadorthi.facts.remote

import br.com.programadorthi.facts.Fact
import io.reactivex.Single

interface RemoteFactsRepository {

    fun fetchCategories(): Single<List<String>>

    fun search(text: String): Single<List<Fact>>
}
