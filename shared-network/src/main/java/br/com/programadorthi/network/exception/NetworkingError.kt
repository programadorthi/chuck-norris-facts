package br.com.programadorthi.network.exception

sealed class NetworkingError(override val message: String) : Exception() {

    object ConnectionTimeout : NetworkingError("Networking operation timed out")

    object InvalidDataFormat : NetworkingError("Invalid response data format")

    object NoInternetConnection : NetworkingError("There is no internet connection")

    class EssentialParamMissing(
        missingParams: String,
        rawObject: Any
    ) : NetworkingError("The $rawObject has missing parameters. They are: $missingParams")

    class UnknownEndpoint(
        override val cause: Throwable?
    ) : NetworkingError("Unknown endpoint. Cause: $cause")

    class UnknownNetworkException(
        override val cause: Throwable?
    ) : NetworkingError("Unknown network exception. Cause: $cause")

    fun needsReport(): Boolean =
        this is UnknownEndpoint ||
                this is EssentialParamMissing ||
                this is InvalidDataFormat ||
                this is UnknownNetworkException
}
