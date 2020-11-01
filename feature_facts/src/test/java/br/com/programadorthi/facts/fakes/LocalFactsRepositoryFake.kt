package br.com.programadorthi.facts.fakes

import br.com.programadorthi.facts.data.local.LocalFactsRepository

class LocalFactsRepositoryFake(
    var categories: List<String> = emptyList(),
    var lastSearches: List<String> = emptyList()
) : LocalFactsRepository {
    override suspend fun getCategories(): List<String> = categories
    override suspend fun getLastSearches(): List<String> = lastSearches
    override suspend fun saveCategories(categories: List<String>) {
        this.categories = categories
    }

    override suspend fun saveNewSearch(text: String) {
        this.lastSearches = lastSearches.toMutableList() + text
    }
}
