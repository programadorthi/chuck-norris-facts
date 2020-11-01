package br.com.programadorthi.domain

sealed class ResultTypes<out T> : Result<T> {
    data class Success<T>(override val value: T) : ResultTypes<T>()
    open class Business : ResultTypes<Nothing>() {
        override val value: Any? get() = null
    }
    data class Error(val cause: Throwable) : ResultTypes<Nothing>() {
        override val value: Any? get() = null
    }
}