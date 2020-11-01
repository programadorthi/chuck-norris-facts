package br.com.programadorthi.facts.fakes

import br.com.programadorthi.domain.persist.PreferencesManager

class PreferencesManagerFake(
    var lastSearches: String = ""
) : PreferencesManager {
    override fun getItem(key: String): String = lastSearches

    override fun putItem(key: String, item: String) {
        lastSearches = item
    }
}
