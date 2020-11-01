package br.com.programadorthi.facts.fakes

import br.com.programadorthi.domain.Result
import br.com.programadorthi.domain.ResultTypes
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.facts.domain.FactsUseCase

class FactsUseCaseFake(
    var categories: List<String> = emptyList(),
    var lastSearches: List<String> = emptyList(),
    var searchResult: List<Fact> = emptyList(),
    var categoriesException: Throwable? = null,
    var searchException: Throwable? = null
) : FactsUseCase {
    override suspend fun categories(limit: Int, shuffle: Boolean): Result<List<String>> {
        if (categoriesException != null) {
            return ResultTypes.Error(categoriesException!!)
        }
        val result = when {
            limit <= 0 -> emptyList()
            limit > categories.size -> categories
            else -> categories.slice(IntRange(0, limit - 1))
        }
        return ResultTypes.Success(result)
    }

    override suspend fun lastSearches(): Result<List<String>> = ResultTypes.Success(lastSearches)

    override suspend fun search(text: String): Result<List<Fact>> {
        if (searchException != null) {
            return ResultTypes.Error(searchException!!)
        }
        return ResultTypes.Success(searchResult)
    }
}
