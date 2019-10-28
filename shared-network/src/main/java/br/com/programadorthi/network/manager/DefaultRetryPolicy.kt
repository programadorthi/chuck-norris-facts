package br.com.programadorthi.network.manager

import br.com.programadorthi.network.exception.NetworkingError
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

class DefaultRetryPolicy(
    private val scheduler: Scheduler,
    private val attempts: Int = DEFAULT_ATTEMPTS
) : Function<Flowable<Throwable>, Publisher<*>> {

    override fun apply(flowableError: Flowable<Throwable>): Publisher<*> = flowableError.zipWith(
        Flowable.range(1, attempts + 1),
        BiFunction<Throwable, Int, Int> { throwable, attempt ->
            if (attempt > attempts) {
                throw throwable
            }

            when (throwable) {
                is NetworkingError.NoInternetConnection,
                is NetworkingError.ConnectionTimeout,
                is NetworkingError.UnknownNetworkException -> {
                    return@BiFunction attempt * FIRST_ATTEMPT_IN_SECONDS
                }
                else -> throw throwable
            }
        }
    ).flatMap { timeInSeconds ->
        Flowable.timer(timeInSeconds.toLong(), TimeUnit.SECONDS, scheduler)
    }

    private companion object {
        private const val DEFAULT_ATTEMPTS = 2
        private const val FIRST_ATTEMPT_IN_SECONDS = 4
    }
}
