package br.com.programadorthi.network.exception

import br.com.programadorthi.domain.report.CrashReport
import io.reactivex.functions.Function
import kotlinx.serialization.SerializationException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkingErrorMapper(
    private val crashReport: CrashReport
) : Function<Throwable, NetworkingError> {
    override fun apply(cause: Throwable): NetworkingError {
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
