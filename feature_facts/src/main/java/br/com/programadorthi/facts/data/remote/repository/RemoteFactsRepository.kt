package br.com.programadorthi.facts.data.remote.repository

import br.com.programadorthi.facts.domain.Fact

internal interface RemoteFactsRepository {
    suspend fun fetchCategories(): List<String>
    suspend fun search(text: String): List<Fact>
}
