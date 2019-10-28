package br.com.programadorthi.network.fake

import io.reactivex.Flowable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher

class DefaultRetryPolicyFake : Function<Flowable<Throwable>, Publisher<*>> {

    override fun apply(flowableError: Flowable<Throwable>): Publisher<*> = flowableError
}
