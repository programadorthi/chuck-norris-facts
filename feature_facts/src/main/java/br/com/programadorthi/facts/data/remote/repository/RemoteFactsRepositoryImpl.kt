package br.com.programadorthi.facts.data.remote.repository

import br.com.programadorthi.facts.data.remote.FactsService
import br.com.programadorthi.facts.data.remote.raw.FactsResponseRaw
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.network.manager.NetworkManager
import br.com.programadorthi.network.mapper.RemoteMapper

internal class RemoteFactsRepositoryImpl(
    private val factsMapper: RemoteMapper<FactsResponseRaw, List<Fact>>,
    private val factsService: FactsService,
    private val networkManager: NetworkManager
) : RemoteFactsRepository {

    override suspend fun fetchCategories(): List<String> =
        networkManager.performAndReturnsData(
            request = factsService::fetchCategories
        )

    override suspend fun search(text: String): List<Fact> =
        networkManager.performAndReturnsMappedData(
            mapper = factsMapper,
            request = { factsService.search(query = text) }
        )
}
