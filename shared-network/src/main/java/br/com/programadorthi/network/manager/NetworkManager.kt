package br.com.programadorthi.network.manager

interface NetworkManager {
    suspend fun performAndDone(request: suspend () -> Unit)
    suspend fun <Data> performAndReturnsData(request: suspend () -> Data): Data
    suspend fun <From, To> performAndReturnsMappedData(
        mapper: (From) -> To,
        request: suspend () -> From
    ): To
}
