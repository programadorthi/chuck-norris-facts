package br.com.programadorthi.facts

import br.com.programadorthi.facts.local.LocalFactsRepository
import br.com.programadorthi.facts.remote.repository.RemoteFactsRepository
import io.reactivex.Single

class FactsRepositoryImpl(
    private val localFactsRepository: LocalFactsRepository,
    private val remoteFactsRepository: RemoteFactsRepository
) : FactsRepository {

    override fun fetchCategories(offset: Int, shuffle: Boolean): Single<List<String>> {
        return localFactsRepository.getCategories()
            .flatMap { categories ->
                if (categories.isEmpty()) {
                    return@flatMap remoteFactsRepository
                        .fetchCategories()
                        .doOnSuccess { localFactsRepository.saveCategories(it) }
                }
                return@flatMap Single.just(categories)
            }
            .toObservable()
            .flatMapIterable { categories -> if (shuffle) categories.shuffled() else categories }
            .take(offset.toLong())
            .toList()
    }

    override fun getLastSearches(): Single<List<String>> = localFactsRepository.getLastSearches()

    override fun doSearch(text: String): Single<List<Fact>> {
        return remoteFactsRepository.search(text)
            .doOnSuccess { localFactsRepository.saveNewSearch(text) }
    }
}
