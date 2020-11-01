package br.com.programadorthi.facts.data.remote.repository

import br.com.programadorthi.facts.domain.Fact
import io.reactivex.Single

interface RemoteFactsRepository {

    fun fetchCategories(): Single<List<String>>

    fun search(text: String): Single<List<Fact>>
}
