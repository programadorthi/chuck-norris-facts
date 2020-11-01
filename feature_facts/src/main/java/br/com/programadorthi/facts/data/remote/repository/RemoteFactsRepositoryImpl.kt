package br.com.programadorthi.facts.data.remote.repository

import br.com.programadorthi.facts.data.remote.FactsService
import br.com.programadorthi.facts.data.remote.raw.FactsResponseRaw
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.network.manager.NetworkManager
import br.com.programadorthi.network.mapper.RemoteMapper
import io.reactivex.Single

class RemoteFactsRepositoryImpl(
    private val factsMapper: RemoteMapper<FactsResponseRaw, List<Fact>>,
    private val factsService: FactsService,
    private val networkManager: NetworkManager
) : RemoteFactsRepository {

    override fun fetchCategories(): Single<List<String>> {
        return networkManager.performAndReturnsData(
            request = factsService.fetchCategories()
        )
    }

    override fun search(text: String): Single<List<Fact>> {
        return networkManager.performAndReturnsMappedData(
            mapper = factsMapper,
            request = factsService.search(query = text)
        )
    }
}
