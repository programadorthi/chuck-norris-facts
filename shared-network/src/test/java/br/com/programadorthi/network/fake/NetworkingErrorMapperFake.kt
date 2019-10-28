package br.com.programadorthi.network.fake

import br.com.programadorthi.domain.report.CrashReport
import br.com.programadorthi.network.exception.NetworkingError
import io.reactivex.functions.Function

class NetworkingErrorMapperFake(
    private val crashReport: CrashReport,
    var causeToReport: Throwable? = null,
    var networkingError: NetworkingError? = null
) : Function<Throwable, NetworkingError> {
    override fun apply(cause: Throwable): NetworkingError {
        if (cause == causeToReport) {
            crashReport.report(cause)
        }
        return networkingError ?: NetworkingError.NoInternetConnection
    }
}
