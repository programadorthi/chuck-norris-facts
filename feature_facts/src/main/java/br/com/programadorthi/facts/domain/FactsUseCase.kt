package br.com.programadorthi.facts.domain

import br.com.programadorthi.domain.Result

interface FactsUseCase {
    suspend fun categories(limit: Int, shuffle: Boolean): Result<List<String>>
    suspend fun lastSearches(): Result<List<String>>
    suspend fun search(text: String): Result<List<Fact>>
}
