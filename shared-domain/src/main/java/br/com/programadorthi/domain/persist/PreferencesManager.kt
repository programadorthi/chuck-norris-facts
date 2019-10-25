package br.com.programadorthi.domain.persist

interface PreferencesManager {
    fun getItems(key: String): Set<String>

    fun putItems(key: String, items: Set<String>)
}
