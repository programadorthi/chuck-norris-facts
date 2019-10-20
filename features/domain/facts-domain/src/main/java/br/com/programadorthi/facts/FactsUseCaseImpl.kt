package br.com.programadorthi.facts

import io.reactivex.Single

class FactsUseCaseImpl(private val factsRepository: FactsRepository) : FactsUseCase {

    override fun categories(offset: Int): Single<List<String>> {
        if (offset <= 0) {
            return Single.just(emptyList())
        }

        return factsRepository.fetchCategories(offset)
    }

    override fun search(text: String): Single<List<Fact>> {
        return when {
            text.isBlank() -> Single.error(FactsBusiness.EmptySearch)
            else -> factsRepository.doSearch(text)
        }
    }
}
