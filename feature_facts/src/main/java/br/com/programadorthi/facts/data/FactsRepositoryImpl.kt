package br.com.programadorthi.facts.data

import br.com.programadorthi.domain.Result
import br.com.programadorthi.domain.flatMap
import br.com.programadorthi.domain.getOrDefault
import br.com.programadorthi.domain.map
import br.com.programadorthi.domain.mapCatching
import br.com.programadorthi.domain.onSuccess
import br.com.programadorthi.facts.data.local.LocalFactsRepository
import br.com.programadorthi.facts.data.remote.repository.RemoteFactsRepository
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.facts.domain.FactsRepository

internal class FactsRepositoryImpl(
    private val localFactsRepository: LocalFactsRepository,
    private val remoteFactsRepository: RemoteFactsRepository
) : FactsRepository {

    override suspend fun fetchCategories(limit: Int, shuffle: Boolean): Result<List<String>> =
        mapCatching(localFactsRepository::getCategories)
            .flatMap(::checkForUpdateCache)
            .map { categories -> if (shuffle) categories.shuffled() else categories }
            .map { categories -> categories.take(limit) }

    override suspend fun getLastSearches(): Result<List<String>> =
        mapCatching { localFactsRepository.getLastSearches() }

    override suspend fun doSearch(text: String): Result<List<Fact>> =
        mapCatching { remoteFactsRepository.search(text) }
            .onSuccess { localFactsRepository.saveNewSearch(text) }

    private suspend fun checkForUpdateCache(result: Result<List<String>>) = when {
        result.getOrDefault(emptyList()).isNotEmpty() -> result
        else -> mapCatching(remoteFactsRepository::fetchCategories)
            .onSuccess(localFactsRepository::saveCategories)
    }
}
