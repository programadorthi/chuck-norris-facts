package br.com.programadorthi.network.manager

import br.com.programadorthi.network.ConnectionCheck
import br.com.programadorthi.network.exception.NetworkingError
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import org.reactivestreams.Publisher

class DefaultNetworkManager(
    private val connectionCheck: ConnectionCheck,
    private val networkingErrorMapper: Function<Throwable, NetworkingError>,
    private val retryPolicy: Function<Flowable<Throwable>, Publisher<*>>
) : NetworkManager {

    override fun performAndDone(request: Completable): Completable {
        return when (connectionCheck.hasInternetConnection()) {
            false -> Completable.error(NetworkingError.NoInternetConnection)
            true -> request
                .onErrorResumeNext { cause ->
                    Completable.error(networkingErrorMapper.apply(cause))
                }
        }.retryWhen(retryPolicy)
    }

    override fun <Data> performAndReturnsData(request: Single<Data>): Single<Data> {
        return when (connectionCheck.hasInternetConnection()) {
            false -> Single.error(NetworkingError.NoInternetConnection)
            true -> request
                .onErrorResumeNext { cause -> Single.error(networkingErrorMapper.apply(cause)) }
        }.retryWhen(retryPolicy)
    }

    override fun <From, To> performAndReturnsMappedData(
        mapper: Function<From, To>,
        request: Single<From>
    ): Single<To> {
        return when (connectionCheck.hasInternetConnection()) {
            false -> Single.error(NetworkingError.NoInternetConnection)
            true -> request
                .map(mapper)
                .onErrorResumeNext { cause -> Single.error(networkingErrorMapper.apply(cause)) }
        }.retryWhen(retryPolicy)
    }
}
