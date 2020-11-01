package br.com.programadorthi.domain

suspend fun <T> mapCatching(action: suspend () -> T): Result<T> = try {
    ResultTypes.Success(action.invoke())
} catch (ex: Throwable) {
    ResultTypes.Error(ex)
}

fun <T> Result<T>.exceptionOrNull(): Throwable? =
    when (this) {
        is ResultTypes.Error -> cause
        else -> null
    }

fun <R, T : R> Result<T>.getOrDefault(defaultValue: R): R {
    if (this is ResultTypes.Error) return defaultValue
    @Suppress("UNCHECKED_CAST")
    return value as T
}

suspend fun <R, T> Result<T>.flatMap(transform: suspend (Result<T>) -> Result<R>): Result<R> =
    when (this) {
        is ResultTypes.Success -> transform(this)
        is ResultTypes.Error -> ResultTypes.Error(cause)
        else -> throw IllegalStateException("Invalid Result type to map: $this")
    }

@Suppress("UNCHECKED_CAST")
suspend fun <R, T> Result<T>.map(transform: suspend (T) -> R): Result<R> =
    flatMap { other -> ResultTypes.Success(transform(other.value as T)) }

@Suppress("UNCHECKED_CAST")
suspend fun <T> Result<T>.onSuccess(action: suspend (T) -> Unit): Result<T> =
    this.also { if (it is ResultTypes.Success) action.invoke(value as T) }

suspend fun <T> Result<T>.onError(action: suspend (Throwable) -> Unit): Result<T> =
    this.also { exceptionOrNull()?.run { action.invoke(this) } }