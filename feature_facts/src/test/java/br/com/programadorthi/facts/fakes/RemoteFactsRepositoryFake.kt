package br.com.programadorthi.facts.fakes

import br.com.programadorthi.facts.data.remote.repository.RemoteFactsRepository
import br.com.programadorthi.facts.domain.Fact

class RemoteFactsRepositoryFake(
    var categories: List<String> = emptyList(),
    var facts: List<Fact> = emptyList(),
    var fetchCategoriesError: Throwable? = null,
    var searchError: Throwable? = null
) : RemoteFactsRepository {
    override suspend fun fetchCategories(): List<String> {
        if (fetchCategoriesError != null) {
            throw fetchCategoriesError!!
        }
        return categories
    }

    override suspend fun search(text: String): List<Fact> {
        if (searchError != null) {
            throw searchError!!
        }
        return facts
    }
}
