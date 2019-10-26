package br.com.programadorthi.facts.fake

import br.com.programadorthi.facts.local.LocalFactsRepository
import io.reactivex.Single

class LocalFactsRepositoryFake(
    var categories: List<String> = emptyList(),
    var lastSearches: List<String> = emptyList()
) : LocalFactsRepository {
    override fun getCategories(): Single<List<String>> = Single.just(categories)

    override fun getLastSearches(): Single<List<String>> = Single.just(lastSearches)

    override fun saveCategories(categories: List<String>) {
        this.categories = categories
    }

    override fun saveNewSearch(text: String) {
        lastSearches = lastSearches.toMutableList() + text
    }
}
