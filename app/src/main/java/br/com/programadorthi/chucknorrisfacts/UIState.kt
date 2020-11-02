package br.com.programadorthi.chucknorrisfacts

sealed class UIState<out T> {
    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Failed(val cause: Throwable?) : UIState<Nothing>()
    data class Success<R>(val data: R) : UIState<R>()
}
