package br.com.programadorthi.facts.local

import br.com.programadorthi.domain.persist.PreferencesManager
import io.reactivex.Single

class LocalFactsRepositoryImpl(
    private val preferencesManager: PreferencesManager
) : LocalFactsRepository {

    override fun getCategories(): Single<List<String>> {
        return Single.just(preferencesManager.getItems(CATEGORIES_KEY).toList())
    }

    override fun getLastSearches(): Single<List<String>> {
        return Single.just(getLastSearchesSet().toList())
    }

    override fun saveCategories(categories: List<String>) {
        preferencesManager.putItems(CATEGORIES_KEY, categories.toSet())
    }

    override fun saveNewSearch(text: String) {
        val lastSearches = getLastSearchesSet()
        preferencesManager.putItems(LAST_SEARCHES_KEY, lastSearches + text)
    }

    private fun getLastSearchesSet() = preferencesManager.getItems(LAST_SEARCHES_KEY)

    private companion object {
        private const val CATEGORIES_KEY = "categories"
        private const val LAST_SEARCHES_KEY = "last_searches"
    }
}
