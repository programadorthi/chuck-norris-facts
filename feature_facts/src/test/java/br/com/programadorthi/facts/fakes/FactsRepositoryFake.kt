package br.com.programadorthi.facts.fakes

import br.com.programadorthi.domain.Result
import br.com.programadorthi.domain.ResultTypes
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.facts.domain.FactsRepository

class FactsRepositoryFake(
    var categories: List<String> = emptyList(),
    var lastSearches: List<String> = emptyList(),
    var termsAndResults: Map<String, List<Fact>> = emptyMap()
) : FactsRepository {
    override suspend fun fetchCategories(limit: Int, shuffle: Boolean): Result<List<String>> {
        val result = if (limit > categories.size) {
            categories
        } else {
            categories.slice(IntRange(start = 0, endInclusive = limit - 1))
        }
        return ResultTypes.Success(result)
    }

    override suspend fun getLastSearches(): Result<List<String>> = ResultTypes.Success(lastSearches)

    override suspend fun doSearch(text: String): Result<List<Fact>> =
        ResultTypes.Success(termsAndResults[text] ?: emptyList())
}
