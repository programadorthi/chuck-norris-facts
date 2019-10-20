package br.com.programadorthi.facts

import io.reactivex.Single

class FactsUseCaseImpl(private val factsRepository: FactsRepository) : FactsUseCase {

    override fun categories(): Single<List<String>> {
        return factsRepository.fetchCategories()
    }

    override fun search(text: String): Single<List<Fact>> {
        return when {
            text.isBlank() -> Single.error(FactsBusiness.EmptySearch)
            else -> factsRepository.doSearch(text)
        }
    }
}
