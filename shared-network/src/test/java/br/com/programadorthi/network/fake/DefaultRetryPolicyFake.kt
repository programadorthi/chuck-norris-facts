package br.com.programadorthi.network.fake

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Publisher

class DefaultRetryPolicyFake(
    val publisher: PublishSubject<Any> = PublishSubject.create(),
    var throwException: Boolean = true
) : Function<Flowable<Throwable>, Publisher<*>> {

    override fun apply(flowableError: Flowable<Throwable>): Publisher<*> = flowableError
        .flatMap { throwable ->
            if (throwException) {
                throw throwable
            }
            publisher.toFlowable(BackpressureStrategy.LATEST)
        }
}
