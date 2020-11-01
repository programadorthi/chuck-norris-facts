package br.com.programadorthi.chucknorrisfacts.di

import br.com.programadorthi.chucknorrisfacts.BuildConfig
import br.com.programadorthi.chucknorrisfacts.network.ConnectionCheckImpl
import br.com.programadorthi.network.ConnectionCheck
import br.com.programadorthi.network.RetrofitBuilder
import br.com.programadorthi.network.sharedNetworkModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import retrofit2.Retrofit
import timber.log.Timber

private const val HTTP_LOGGING_INTERCEPTOR = "HTTP_LOGGING_INTERCEPTOR"

val networkModule = DI.Module("networkModule") {
    import(sharedNetworkModule)

    bind<ConnectionCheck>() with singleton { ConnectionCheckImpl(instance()) }
    bind<Retrofit>() with singleton {
        RetrofitBuilder(
            endpoint = BuildConfig.BASE_URL,
            httpClient = instance()
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
