package br.com.programadorthi.network.manager

import br.com.programadorthi.network.ConnectionCheck
import br.com.programadorthi.network.exception.NetworkingError
import br.com.programadorthi.network.exception.NetworkingErrorMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultNetworkManager(
    private val connectionCheck: ConnectionCheck,
    private val networkingErrorMapper: NetworkingErrorMapper,
    private val ioDispatcher: CoroutineDispatcher
) : NetworkManager {
    override suspend fun performAndDone(request: suspend () -> Unit) {
        withContext(ioDispatcher) {
            checkConnection()
            try {
                request.invoke()
            } catch (ex: Throwable) {
                throw networkingErrorMapper.mapper(ex)
            }
        }
    }

    override suspend fun <Data> performAndReturnsData(request: suspend () -> Data): Data =
        withContext(ioDispatcher) {
            checkConnection()
            try {
                request.invoke()
            } catch (ex: Throwable) {
                throw networkingErrorMapper.mapper(ex)
            }
        }

    override suspend fun <From, To> performAndReturnsMappedData(
        mapper: (From) -> To,
        request: suspend () -> From
    ): To = withContext(ioDispatcher) {
        checkConnection()
        try {
            mapper.invoke(request.invoke())
        } catch (ex: Throwable) {
            throw networkingErrorMapper.mapper(ex)
        }
    }

    private fun checkConnection() {
        if (connectionCheck.hasInternetConnection().not()) {
            throw NetworkingError.NoInternetConnection
        }
    }
}
