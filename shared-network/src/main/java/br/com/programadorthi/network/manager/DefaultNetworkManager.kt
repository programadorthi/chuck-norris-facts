package br.com.programadorthi.network.manager

import br.com.programadorthi.domain.report.CrashReport
import br.com.programadorthi.network.ConnectionCheck
import br.com.programadorthi.network.exception.NetworkingError
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.Function
import kotlinx.serialization.SerializationException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DefaultNetworkManager(
    private val crashReport: CrashReport,
    private val connectionCheck: ConnectionCheck,
    private val scheduler: Scheduler
) : NetworkManager {

    override fun performAndDone(request: Completable): Completable {
        return when (connectionCheck.hasInternetConnection()) {
            false -> Completable.error(NetworkingError.NoInternetConnection)
            true -> request
                .subscribeOn(scheduler)
                .onErrorResumeNext { cause -> Completable.error(mapperException(cause)) }
        }
    }

    override fun <Data> performAndReturnsData(request: Single<Data>): Single<Data> {
        return when (connectionCheck.hasInternetConnection()) {
            false -> Single.error(NetworkingError.NoInternetConnection)
            true -> request
                .subscribeOn(scheduler)
                .onErrorResumeNext { cause -> Single.error(mapperException(cause)) }
        }
    }

    override fun <From, To> performAndReturnsMappedData(
        mapper: Function<From, To>,
        request: Single<From>
    ): Single<To> {
        return when (connectionCheck.hasInternetConnection()) {
            false -> Single.error(NetworkingError.NoInternetConnection)
            true -> request
                .subscribeOn(scheduler)
                .map(mapper)
                .onErrorResumeNext { cause -> Single.error(mapperException(cause)) }
        }
    }

    private fun mapperException(cause: Throwable): NetworkingError {
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
