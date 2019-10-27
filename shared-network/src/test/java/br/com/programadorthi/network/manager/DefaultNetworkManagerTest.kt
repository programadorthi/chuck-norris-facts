package br.com.programadorthi.network.manager

import br.com.programadorthi.network.exception.NetworkingError
import br.com.programadorthi.network.fake.ConnectionCheckFake
import br.com.programadorthi.network.fake.CrashReportFake
import br.com.programadorthi.network.fake.RemoteMapperFake
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.UnknownFieldException
import kotlinx.serialization.UpdateNotSupportedException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DefaultNetworkManagerTest {

    private lateinit var connectionCheckFake: ConnectionCheckFake

    private lateinit var crashReportFake: CrashReportFake

    private lateinit var networkManager: NetworkManager

    @Before
    fun `before each test`() {
        connectionCheckFake = ConnectionCheckFake()

        crashReportFake = CrashReportFake()

        networkManager = DefaultNetworkManager(
            crashReport = crashReportFake,
            connectionCheck = connectionCheckFake
        )
    }

    @Test
    fun `should success when all works fine`() {
        val completableResult = networkManager.performAndDone(Completable.complete()).test()

        completableResult
            .assertNoErrors()
            .assertNoValues()
            .assertComplete()

        val singleData = "1234"

        val singleResult = networkManager.performAndReturnsData(Single.just(singleData)).test()

        singleResult
            .assertNoErrors()
            .assertComplete()
            .assertValue(singleData)

        val singleMappedResult = networkManager.performAndReturnsMappedData(
            RemoteMapperFake(), Single.just(singleData)
        ).test()

        singleMappedResult
            .assertNoErrors()
            .assertComplete()
            .assertValue(Integer.MAX_VALUE)
    }

    @Test
    fun `should has NoInternetConnection error when there is no internet connection`() {
        connectionCheckFake.hasConnection = false

        val completableResult = networkManager.performAndDone(Completable.never()).test()

        completableResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.NoInternetConnection }
            .assertOf { assertThat(crashReportFake.reported).isNull() }

        val singleResult = networkManager.performAndReturnsData(Single.never<Nothing>()).test()

        singleResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.NoInternetConnection }
            .assertOf { assertThat(crashReportFake.reported).isNull() }

        val singleMappedResult = networkManager.performAndReturnsMappedData(
            RemoteMapperFake(), Single.never<String>()
        ).test()

        singleMappedResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.NoInternetConnection }
            .assertOf { assertThat(crashReportFake.reported).isNull() }
    }

    @Test
    fun `should has EssentialParamMissing error when is missing required fields in the server response data`() {
        val singleMappedResult = networkManager.performAndReturnsMappedData(
            RemoteMapperFake(throwException = true), Single.just("")
        ).test()

        singleMappedResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.EssentialParamMissing }
            .assertOf { assertThat(crashReportFake.reported).isNotNull() }
    }

    @Test
    fun `should has InvalidDataFormat error when the server response json data has invalid format`() {
        val completableError = MissingFieldException("field")

        val completableResult = networkManager.performAndDone(
            Completable.error(completableError)
        ).test()

        completableResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.InvalidDataFormat }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(completableError) }

        val singleError = UnknownFieldException(0)

        val singleResult = networkManager.performAndReturnsData(
            Single.error<Nothing>(singleError)
        ).test()

        singleResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.InvalidDataFormat }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(singleError) }

        val singleMappedError = UpdateNotSupportedException("class")

        val singleMappedResult = networkManager.performAndReturnsMappedData(
            RemoteMapperFake(), Single.error<String>(singleMappedError)
        ).test()

        singleMappedResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.InvalidDataFormat }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(singleMappedError) }
    }

    @Test
    fun `should has ConnectionTimeout error when there is a slow internet connection`() {
        val completableResult = networkManager.performAndDone(
            Completable.error(SocketTimeoutException())
        ).test()

        completableResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.ConnectionTimeout }
            .assertOf { assertThat(crashReportFake.reported).isNull() }

        val singleResult = networkManager.performAndReturnsData(
            Single.error<Nothing>(SocketTimeoutException())
        ).test()

        singleResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.ConnectionTimeout }
            .assertOf { assertThat(crashReportFake.reported).isNull() }

        val singleMappedResult = networkManager.performAndReturnsMappedData(
            RemoteMapperFake(), Single.error<String>(SocketTimeoutException())
        ).test()

        singleMappedResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.ConnectionTimeout }
            .assertOf { assertThat(crashReportFake.reported).isNull() }
    }

    @Test
    fun `should has UnknownEndpoint error when the endpoint is unreachable`() {
        val completableError = UnknownHostException("one")

        val completableResult = networkManager.performAndDone(
            Completable.error(completableError)
        ).test()

        completableResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.UnknownEndpoint }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(completableError) }

        val singleError = UnknownHostException("two")

        val singleResult = networkManager.performAndReturnsData(
            Single.error<Nothing>(singleError)
        ).test()

        singleResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.UnknownEndpoint }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(singleError) }

        val singleMappedError = UnknownHostException("three")

        val singleMappedResult = networkManager.performAndReturnsMappedData(
            RemoteMapperFake(), Single.error<String>(singleMappedError)
        ).test()

        singleMappedResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.UnknownEndpoint }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(singleMappedError) }
    }

    @Test
    fun `should has UnknownNetworkException error when throw a generic error`() {
        val completableError = IOException("one")

        val completableResult = networkManager.performAndDone(
            Completable.error(completableError)
        ).test()

        completableResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.UnknownNetworkException }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(completableError) }

        val singleError = IOException("two")

        val singleResult = networkManager.performAndReturnsData(
            Single.error<Nothing>(singleError)
        ).test()

        singleResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.UnknownNetworkException }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(singleError) }

        val singleMappedError = IOException("three")

        val singleMappedResult = networkManager.performAndReturnsMappedData(
            RemoteMapperFake(), Single.error<String>(singleMappedError)
        ).test()

        singleMappedResult
            .assertNotComplete()
            .assertNoValues()
            .assertError { it is NetworkingError.UnknownNetworkException }
            .assertOf { assertThat(crashReportFake.reported).isEqualTo(singleMappedError) }
    }
}
