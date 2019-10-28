package br.com.programadorthi.chucknorrisfacts.preferences

import android.content.SharedPreferences
import br.com.programadorthi.domain.persist.PreferencesManager

class PreferencesManagerImpl(private val preferences: SharedPreferences) : PreferencesManager {

    override fun getItem(key: String): String {
        return preferences.getString(key, EMPTY) ?: EMPTY
    }

    override fun putItem(key: String, item: String) {
        preferences.edit().putString(key, item).apply()
    }

    private companion object {
        private const val EMPTY = ""
    }
}
