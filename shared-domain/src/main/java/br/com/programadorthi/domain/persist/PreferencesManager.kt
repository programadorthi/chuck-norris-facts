package br.com.programadorthi.domain.persist

interface PreferencesManager {
    fun <T> getItems(key: String): Set<T>

    fun <T> putItems(key: String, items: Set<T>)
}
