package br.com.programadorthi.network.manager

import br.com.programadorthi.network.exception.NetworkingError
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.concurrent.TimeUnit

class DefaultRetryPolicyTest {

    private lateinit var testScheduler: TestScheduler

    private lateinit var defaultRetryPolicy: DefaultRetryPolicy

    @Before
    fun `before each test`() {
        testScheduler = TestScheduler()

        defaultRetryPolicy = DefaultRetryPolicy(testScheduler)
    }

    @Test
    fun `should throw a retried NetworkingError when finish three attempts`() {
        val publish: Subject<Throwable> = PublishSubject.create()

        val testSubscriber = TestSubscriber.create<Any>()

        val expected = NetworkingError.NoInternetConnection

        defaultRetryPolicy
            .apply(publish.toFlowable(BackpressureStrategy.LATEST))
            .subscribe(testSubscriber)

        publish.onNext(NetworkingError.ConnectionTimeout)
        publish.onNext(NetworkingError.UnknownNetworkException(expected))
        publish.onNext(expected)

        testSubscriber.awaitTerminalEvent()

        testSubscriber
            .assertNotComplete()
            .assertNoValues()
            .assertError(expected)
    }

    @Test
    fun `should throw the original exception when is not an exception to retry`() {
        val publish: Subject<Throwable> = PublishSubject.create()

        val testSubscriber = TestSubscriber.create<Any>()

        val expected = IOException()

        defaultRetryPolicy
            .apply(publish.toFlowable(BackpressureStrategy.LATEST))
            .subscribe(testSubscriber)

        publish.onNext(expected)

        testSubscriber.awaitTerminalEvent()

        testSubscriber
            .assertNotComplete()
            .assertNoValues()
            .assertError(expected)
    }

    @Test
    fun `should retry after four seconds when is the first attempt`() {
        val expected = 0L

        val testSubscriber = TestSubscriber.create<Any>()

        val publish: Subject<Throwable> = PublishSubject.create()

        val exception = NetworkingError.NoInternetConnection

        defaultRetryPolicy
            .apply(publish.toFlowable(BackpressureStrategy.LATEST))
            .subscribe(testSubscriber)

        publish.onNext(exception)

        testScheduler.advanceTimeBy(4, TimeUnit.SECONDS)

        publish.onComplete()

        testSubscriber.awaitTerminalEvent()

        testSubscriber
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValues(expected)
    }

    @Test
    fun `should retry after eight seconds when is the second attempt`() {
        val expected = 0L

        val testSubscriber = TestSubscriber.create<Any>()

        val publish: Subject<Throwable> = PublishSubject.create()

        val exception = NetworkingError.NoInternetConnection

        defaultRetryPolicy
            .apply(publish.toFlowable(BackpressureStrategy.LATEST))
            .subscribe(testSubscriber)

        publish.onNext(exception)

        testScheduler.advanceTimeBy(4, TimeUnit.SECONDS)

        publish.onNext(exception)

        testScheduler.advanceTimeBy(8, TimeUnit.SECONDS)

        publish.onComplete()

        testSubscriber.awaitTerminalEvent()

        testSubscriber
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(expected, expected)
    }

    @Test
    fun `should throw the current exception when all attempts finish without success`() {
        val testSubscriber = TestSubscriber.create<Any>()

        val publish: Subject<Throwable> = PublishSubject.create()

        val expected = NetworkingError.NoInternetConnection

        defaultRetryPolicy
            .apply(publish.toFlowable(BackpressureStrategy.LATEST))
            .subscribe(testSubscriber)

        publish.onNext(expected)

        testScheduler.advanceTimeBy(4, TimeUnit.SECONDS)

        publish.onNext(expected)

        testScheduler.advanceTimeBy(8, TimeUnit.SECONDS)

        publish.onNext(expected)

        testSubscriber.awaitTerminalEvent()

        testSubscriber
            .assertNotComplete()
            .assertValueCount(2)
            .assertValues(0L, 0L)
            .assertError(expected)
    }
}
