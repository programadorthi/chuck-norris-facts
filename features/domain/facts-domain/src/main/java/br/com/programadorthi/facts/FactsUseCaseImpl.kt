package br.com.programadorthi.facts

import io.reactivex.Single

class FactsUseCaseImpl(private val factsRepository: FactsRepository) : FactsUseCase {

    override fun categories(offset: Int, shuffle: Boolean): Single<List<String>> {
        if (offset <= MIN_OFFSET) {
            return Single.just(emptyList())
        }

        return factsRepository.fetchCategories(offset, shuffle)
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
