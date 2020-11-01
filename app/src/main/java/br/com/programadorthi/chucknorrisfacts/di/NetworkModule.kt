package br.com.programadorthi.chucknorrisfacts.di

import br.com.programadorthi.chucknorrisfacts.BuildConfig
import br.com.programadorthi.chucknorrisfacts.network.ConnectionCheckImpl
import br.com.programadorthi.network.ConnectionCheck
import br.com.programadorthi.network.RetrofitBuilder
import br.com.programadorthi.network.exception.NetworkingError
import br.com.programadorthi.network.exception.NetworkingErrorMapper
import br.com.programadorthi.network.manager.DefaultNetworkManager
import br.com.programadorthi.network.manager.DefaultRetryPolicy
import br.com.programadorthi.network.manager.NetworkManager
import io.reactivex.Flowable
import io.reactivex.functions.Function
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import org.reactivestreams.Publisher
import retrofit2.Retrofit
import timber.log.Timber

private const val HTTP_LOGGING_INTERCEPTOR = "HTTP_LOGGING_INTERCEPTOR"
private const val DEFAULT_RETRY_POLICY = "DEFAULT_RETRY_POLICY"
private const val NETWORKING_ERROR_MAPPER = "NETWORKING_ERROR_MAPPER"

val networkModule = DI.Module("networkModule") {
    bind<ConnectionCheck>() with singleton { ConnectionCheckImpl(instance()) }
    bind<NetworkManager>() with singleton {
        DefaultNetworkManager(
            connectionCheck = instance(),
            networkingErrorMapper = instance(NETWORKING_ERROR_MAPPER),
            retryPolicy = instance(DEFAULT_RETRY_POLICY)
        )
    }
    bind<Retrofit>() with singleton {
        RetrofitBuilder(
            endpoint = BuildConfig.BASE_URL,
            httpClient = instance()
        )
    }
    bind<Function<Flowable<Throwable>, Publisher<*>>>(DEFAULT_RETRY_POLICY) with provider {
        DefaultRetryPolicy(
            scheduler = instance()
        )
    }
    bind<Function<Throwable, NetworkingError>>(NETWORKING_ERROR_MAPPER) with provider {
        NetworkingErrorMapper(
            crashReport = instance()
        )
    }
    bind<HttpLoggingInterceptor.Logger>() with provider {
        object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (BuildConfig.DEBUG) {
                    Timber.d(message)
                }
            }
        }
    }
    bind<Interceptor>(HTTP_LOGGING_INTERCEPTOR) with provider {
        HttpLoggingInterceptor(instance()).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    bind<OkHttpClient>() with provider {
        OkHttpClient.Builder()
            .addInterceptor(instance<Interceptor>(HTTP_LOGGING_INTERCEPTOR))
            .build()
    }
}
