package br.com.programadorthi.network.manager

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function

interface NetworkManager {
    fun performAndDone(request: Completable): Completable
    fun <Data> performAndReturnsData(request: Single<Data>): Single<Data>
    fun <From, To> performAndReturnsMappedData(
        mapper: Function<From, To>,
        request: Single<From>
    ): Single<To>
}
