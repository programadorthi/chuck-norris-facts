package br.com.programadorthi.facts.domain

import br.com.programadorthi.domain.Result
import br.com.programadorthi.domain.ResultTypes

internal class FactsUseCaseImpl(private val factsRepository: FactsRepository) : FactsUseCase {

    override suspend fun categories(limit: Int, shuffle: Boolean): Result<List<String>> {
        if (limit <= MIN_OFFSET) {
            return ResultTypes.Success(emptyList())
        }

        return factsRepository.fetchCategories(limit, shuffle)
    }

    override suspend fun lastSearches(): Result<List<String>> = factsRepository.getLastSearches()

    override suspend fun search(text: String): Result<List<Fact>> =
        when {
            text.isBlank() -> FactsBusiness.EmptySearch
            else -> factsRepository.doSearch(text)
        }

    private companion object {
        private const val MIN_OFFSET = 0
    }
}
