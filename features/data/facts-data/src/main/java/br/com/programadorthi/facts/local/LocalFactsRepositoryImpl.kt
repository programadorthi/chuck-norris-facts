package br.com.programadorthi.facts.local

import br.com.programadorthi.domain.persist.PreferencesManager
import io.reactivex.Single
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class LocalFactsRepositoryImpl(
    private val preferencesManager: PreferencesManager
) : LocalFactsRepository {

    override fun getCategories(): Single<List<String>> {
        return Single.just(getList(CATEGORIES_KEY))
    }

    override fun getLastSearches(): Single<List<String>> {
        return Single.just(getList(LAST_SEARCHES_KEY))
            .map { lastSearches -> lastSearches.asReversed() }
    }

    override fun saveCategories(categories: List<String>) {
        saveList(CATEGORIES_KEY, categories)
    }

    override fun saveNewSearch(text: String) {
        val lastSearches = getList(LAST_SEARCHES_KEY).toSet()
        val withNewTerm = lastSearches + text
        saveList(LAST_SEARCHES_KEY, withNewTerm.toList())
    }

    private fun getList(key: String): List<String> {
        val json = preferencesManager.getItem(key)
        if (json.isBlank()) {
            return emptyList()
        }
        return jsonParse.parse(StringSerializer.list, json)
    }

    private fun saveList(key: String, items: List<String>) {
        val json = jsonParse.stringify(StringSerializer.list, items)
        preferencesManager.putItem(key, json)
    }

    private companion object {
        private const val CATEGORIES_KEY = "categories"
        private const val LAST_SEARCHES_KEY = "last_searches"

        private val jsonParse = Json.plain
    }
}
