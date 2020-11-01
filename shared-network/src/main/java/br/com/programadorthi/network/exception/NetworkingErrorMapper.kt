package br.com.programadorthi.network.exception

import br.com.programadorthi.domain.report.CrashReport
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException

interface NetworkingErrorMapper {
    fun mapper(cause: Throwable): NetworkingError
}

internal class NetworkingErrorMapperImpl(
    private val crashReport: CrashReport
) : NetworkingErrorMapper {
    override fun mapper(cause: Throwable): NetworkingError {
        val error = when (cause) {
            is NetworkingError.EssentialParamMissing -> cause
            is SerializationException -> NetworkingError.InvalidDataFormat
            is SocketTimeoutException -> NetworkingError.ConnectionTimeout
            is UnknownHostException -> NetworkingError.UnknownEndpoint(cause)
            else -> NetworkingError.UnknownNetworkException(cause)
        }

        if (error.needsReport()) {
            crashReport.report(cause)
        }

        return error
    }
}
