package br.com.programadorthi.chucknorrisfacts.ext

import br.com.programadorthi.chucknorrisfacts.R
import br.com.programadorthi.domain.ResultTypes
import br.com.programadorthi.network.exception.NetworkingError

fun ResultTypes.Error.toStringRes() =
    when (cause) {
        is NetworkingError.NoInternetConnection ->
            R.string.no_internet_connection
        else -> R.string.something_wrong
    }