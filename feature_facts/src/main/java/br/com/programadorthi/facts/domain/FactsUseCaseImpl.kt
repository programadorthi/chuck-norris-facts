package br.com.programadorthi.facts.domain

import io.reactivex.Single

class FactsUseCaseImpl(private val factsRepository: FactsRepository) : FactsUseCase {

    override fun categories(limit: Int, shuffle: Boolean): Single<List<String>> {
        if (limit <= MIN_OFFSET) {
            return Single.just(emptyList())
        }

        return factsRepository.fetchCategories(limit, shuffle)
    }

    override fun lastSearches(): Single<List<String>> = factsRepository.getLastSearches()

    override fun search(text: String): Single<List<Fact>> {
        return when {
            text.isBlank() -> Single.error(FactsBusiness.EmptySearch)
            else -> factsRepository.doSearch(text)
        }
    }

    private companion object {
        private const val MIN_OFFSET = 0
    }
}
