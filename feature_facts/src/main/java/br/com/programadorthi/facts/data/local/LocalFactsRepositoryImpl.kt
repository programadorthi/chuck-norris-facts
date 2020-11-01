package br.com.programadorthi.facts.data.local

import br.com.programadorthi.domain.persist.PreferencesManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

internal class LocalFactsRepositoryImpl(
    private val preferencesManager: PreferencesManager,
    private val ioDispatcher: CoroutineDispatcher
) : LocalFactsRepository {

    override suspend fun getCategories(): List<String> = getList(CATEGORIES_KEY)

    override suspend fun getLastSearches(): List<String> =
        getList(LAST_SEARCHES_KEY).asReversed()

    override suspend fun saveCategories(categories: List<String>) {
        saveList(CATEGORIES_KEY, categories)
    }

    override suspend fun saveNewSearch(text: String) {
        val lastSearches = getList(LAST_SEARCHES_KEY).toSet()
        val withNewTerm = lastSearches + text
        saveList(LAST_SEARCHES_KEY, withNewTerm.toList())
    }

    private suspend fun getList(key: String): List<String> =
        withContext(ioDispatcher) {
            val json = preferencesManager.getItem(key)
            when {
                json.isBlank() -> emptyList()
                else -> jsonParse.decodeFromString(ListSerializer(String.serializer()), json)
            }
        }

    private suspend fun saveList(key: String, items: List<String>) =
        withContext(ioDispatcher) {
            val json = jsonParse.encodeToString(ListSerializer(String.serializer()), items)
            preferencesManager.putItem(key, json)
        }

    private companion object {
        private const val CATEGORIES_KEY = "categories"
        private const val LAST_SEARCHES_KEY = "last_searches"

        private val jsonParse = Json
    }
}
