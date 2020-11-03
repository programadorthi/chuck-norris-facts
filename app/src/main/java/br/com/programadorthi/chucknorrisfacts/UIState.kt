package br.com.programadorthi.chucknorrisfacts

import br.com.programadorthi.domain.ResultTypes

sealed class UIState<out T> {
    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Error(val cause: Throwable, val message: String) : UIState<Nothing>()
    data class Failed(val cause: ResultTypes.Business, val message: String) : UIState<Nothing>()
    data class Success<R>(val data: R) : UIState<R>()
}
