package br.com.programadorthi.facts.data.local

internal interface LocalFactsRepository {
    suspend fun getCategories(): List<String>
    suspend fun getLastSearches(): List<String>
    suspend fun saveCategories(categories: List<String>)
    suspend fun saveNewSearch(text: String)
}
