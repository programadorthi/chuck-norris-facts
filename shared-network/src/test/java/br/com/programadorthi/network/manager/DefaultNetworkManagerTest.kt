package br.com.programadorthi.network.manager

import br.com.programadorthi.network.exception.NetworkingError
import br.com.programadorthi.network.exception.NetworkingErrorMapper
import br.com.programadorthi.network.exception.NetworkingErrorMapperImpl
import br.com.programadorthi.network.fake.ConnectionCheckFake
import br.com.programadorthi.network.fake.CrashReportFake
import br.com.programadorthi.network.fake.RemoteMapperFake
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.serialization.SerializationException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test

class DefaultNetworkManagerTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var connectionCheckFake: ConnectionCheckFake
    private lateinit var crashReportFake: CrashReportFake
    private lateinit var networkingErrorMapper: NetworkingErrorMapper
    private lateinit var networkManager: NetworkManager

    @Before
    fun `before each test`() {
        connectionCheckFake = ConnectionCheckFake()

        crashReportFake = CrashReportFake()

        networkingErrorMapper = NetworkingErrorMapperImpl(
            crashReport = crashReportFake
        )

        networkManager = DefaultNetworkManager(
            connectionCheck = connectionCheckFake,
            networkingErrorMapper = networkingErrorMapper,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `should has NoInternetConnection error when there is no internet connection`() =
        testDispatcher.runBlockingTest {
            connectionCheckFake.hasConnection = false

            val func1 = mockk<suspend () -> Unit>(relaxed = true)
            val func2 = mockk<suspend () -> String>(relaxed = true)
            coEvery { func1() } just Runs
            coEvery { func2() } returns "-1"

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndDone(func1)
                }
            }.isEqualTo(NetworkingError.NoInternetConnection)
            assertThat(crashReportFake.reported).isEqualTo(null)

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsData(func2)
                }
            }.isEqualTo(NetworkingError.NoInternetConnection)
            assertThat(crashReportFake.reported).isEqualTo(null)

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsMappedData(RemoteMapperFake(), func2)
                }
            }.isEqualTo(NetworkingError.NoInternetConnection)
            assertThat(crashReportFake.reported).isEqualTo(null)
        }

    @Test
    fun `should has EssentialParamMissing error when is missing required fields in the server response data`() =
        testDispatcher.runBlockingTest {

            val func2 = mockk<suspend () -> String>(relaxed = true)
            coEvery { func2() } returns "-1"

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsMappedData(
                        RemoteMapperFake(throwException = true), func2
                    )
                }
            }.isInstanceOf(NetworkingError.EssentialParamMissing::class.java)
            assertThat(crashReportFake.reported).isNotNull()
        }

    @Test
    fun `should has InvalidDataFormat error when the server response json data has invalid format`() =
        testDispatcher.runBlockingTest {
            val completableError = SerializationException("field")

            val func1 = mockk<suspend () -> Unit>(relaxed = true)
            coEvery { func1() } throws completableError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndDone(func1)
                }
            }.isInstanceOf(NetworkingError.InvalidDataFormat::class.java)
            assertThat(crashReportFake.reported).isEqualTo(completableError)

            val singleError = SerializationException("0")

            val func2 = mockk<suspend () -> String>(relaxed = true)
            coEvery { func2() } throws singleError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsData(func2)
                }
            }.isInstanceOf(NetworkingError.InvalidDataFormat::class.java)
            assertThat(crashReportFake.reported).isEqualTo(singleError)

            val singleMappedError = SerializationException("class")

            coEvery { func2() } throws singleMappedError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsMappedData(RemoteMapperFake(), func2)
                }
            }.isInstanceOf(NetworkingError.InvalidDataFormat::class.java)
            assertThat(crashReportFake.reported).isEqualTo(singleMappedError)
        }

    @Test
    fun `should has ConnectionTimeout error when there is a slow internet connection`() =
        testDispatcher.runBlockingTest {

            val func1 = mockk<suspend () -> Unit>(relaxed = true)
            val func2 = mockk<suspend () -> String>(relaxed = true)
            coEvery { func1() } throws SocketTimeoutException()
            coEvery { func2() } throws SocketTimeoutException()

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndDone(func1)
                }
            }.isInstanceOf(NetworkingError.ConnectionTimeout::class.java)
            assertThat(crashReportFake.reported).isNull()

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsData(func2)
                }
            }.isInstanceOf(NetworkingError.ConnectionTimeout::class.java)
            assertThat(crashReportFake.reported).isNull()

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsMappedData(RemoteMapperFake(), func2)
                }
            }.isInstanceOf(NetworkingError.ConnectionTimeout::class.java)
            assertThat(crashReportFake.reported).isNull()
        }

    @Test
    fun `should has UnknownEndpoint error when the endpoint is unreachable`() =
        testDispatcher.runBlockingTest {
            val completableError = UnknownHostException("one")

            val func1 = mockk<suspend () -> Unit>(relaxed = true)
            coEvery { func1() } throws completableError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndDone(func1)
                }
            }.isInstanceOf(NetworkingError.UnknownEndpoint::class.java)
            assertThat(crashReportFake.reported).isEqualTo(completableError)

            val singleError = UnknownHostException("two")

            val func2 = mockk<suspend () -> String>(relaxed = true)
            coEvery { func2() } throws singleError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsData(func2)
                }
            }.isInstanceOf(NetworkingError.UnknownEndpoint::class.java)
            assertThat(crashReportFake.reported).isEqualTo(singleError)

            val singleMappedError = UnknownHostException("three")

            coEvery { func2() } throws singleMappedError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsMappedData(RemoteMapperFake(), func2)
                }
            }.isInstanceOf(NetworkingError.UnknownEndpoint::class.java)
            assertThat(crashReportFake.reported).isEqualTo(singleMappedError)
        }

    @Test
    fun `should has UnknownNetworkException error when throw a generic error`() =
        testDispatcher.runBlockingTest {
            val completableError = IOException("one")

            val func1 = mockk<suspend () -> Unit>(relaxed = true)
            coEvery { func1() } throws completableError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndDone(func1)
                }
            }.isInstanceOf(NetworkingError.UnknownNetworkException::class.java)
            assertThat(crashReportFake.reported).isEqualTo(completableError)

            val singleError = IOException("two")

            val func2 = mockk<suspend () -> String>(relaxed = true)
            coEvery { func2() } throws singleError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsData(func2)
                }
            }.isInstanceOf(NetworkingError.UnknownNetworkException::class.java)
            assertThat(crashReportFake.reported).isEqualTo(singleError)

            val singleMappedError = IOException("three")

            coEvery { func2() } throws singleMappedError

            assertThatThrownBy {
                runBlocking {
                    networkManager.performAndReturnsMappedData(RemoteMapperFake(), func2)
                }
            }.isInstanceOf(NetworkingError.UnknownNetworkException::class.java)
            assertThat(crashReportFake.reported).isEqualTo(singleMappedError)
        }
}
