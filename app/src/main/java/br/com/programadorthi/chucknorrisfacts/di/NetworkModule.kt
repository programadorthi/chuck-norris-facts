package br.com.programadorthi.chucknorrisfacts.di

import br.com.programadorthi.chucknorrisfacts.BuildConfig
import br.com.programadorthi.chucknorrisfacts.network.ConnectionCheckImpl
import br.com.programadorthi.network.ConnectionCheck
import br.com.programadorthi.network.RetrofitBuilder
import br.com.programadorthi.network.exception.NetworkingErrorMapper
import br.com.programadorthi.network.manager.DefaultNetworkManager
import br.com.programadorthi.network.manager.DefaultRetryPolicy
import br.com.programadorthi.network.manager.NetworkManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

private const val HTTP_LOGGING_INTERCEPTOR = "HTTP_LOGGING_INTERCEPTOR"
private const val DEFAULT_RETRY_POLICY = "DEFAULT_RETRY_POLICY"
private const val NETWORKING_ERROR_MAPPER = "NETWORKING_ERROR_MAPPER"

val networkModule = module {
    single<ConnectionCheck> { ConnectionCheckImpl(get()) }

    single<NetworkManager> {
        DefaultNetworkManager(
            connectionCheck = get(),
            networkingErrorMapper = get(named(NETWORKING_ERROR_MAPPER)),
            retryPolicy = get(named(DEFAULT_RETRY_POLICY))
        )
    }

    single {
        RetrofitBuilder(
            endpoint = BuildConfig.BASE_URL,
            httpClient = get()
        )
    }

    factory(named(DEFAULT_RETRY_POLICY)) { DefaultRetryPolicy(scheduler = get()) }

    factory(named(NETWORKING_ERROR_MAPPER)) { NetworkingErrorMapper(crashReport = get()) }

    factory<HttpLoggingInterceptor.Logger> {
        object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (BuildConfig.DEBUG) {
                    Timber.d(message)
                }
            }
        }
    }

    factory<Interceptor>(named(HTTP_LOGGING_INTERCEPTOR)) {
        HttpLoggingInterceptor(get()).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    factory {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>(named(HTTP_LOGGING_INTERCEPTOR)))
            .build()
    }
}
