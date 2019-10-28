package br.com.programadorthi.domain

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Error(val cause: Throwable) : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
}
