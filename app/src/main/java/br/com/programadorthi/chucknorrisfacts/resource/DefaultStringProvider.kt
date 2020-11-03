package br.com.programadorthi.chucknorrisfacts.resource

import android.content.Context
import androidx.annotation.StringRes
import br.com.programadorthi.domain.resource.StringProvider

class DefaultStringProvider(
    private val context: Context
) : StringProvider {
    override fun getString(@StringRes stringId: Int): String =
        context.getString(stringId)

    override fun getString(@StringRes stringId: Int, vararg args: Any): String =
        context.getString(stringId, args)
}