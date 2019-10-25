package br.com.programadorthi.chucknorrisfacts.preferences

import android.content.SharedPreferences
import br.com.programadorthi.domain.persist.PreferencesManager

class PreferencesManagerImpl(private val preferences: SharedPreferences) : PreferencesManager {

    override fun getItems(key: String): Set<String> {
        return preferences.getStringSet(key, emptySet()) ?: emptySet()
    }

    override fun putItems(key: String, items: Set<String>) {
        preferences.edit().putStringSet(key, items).apply()
    }
}
