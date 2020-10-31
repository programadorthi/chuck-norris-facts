package br.com.programadorthi.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitBuilder {
    private val contentType = "application/json".toMediaType()
    private val jsonFactory = Json {
        allowSpecialFloatingPointValues = true
        ignoreUnknownKeys = true
        isLenient = true
        useArrayPolymorphism = true
    }.asConverterFactory(contentType)
    private val rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create()

    operator fun invoke(
        endpoint: String,
        httpClient: OkHttpClient,
        convertFactory: Converter.Factory = jsonFactory,
        callAdapterFactory: CallAdapter.Factory = rxJavaCallAdapterFactory
    ) = with(Retrofit.Builder()) {
        baseUrl(endpoint)
        client(httpClient)
        addConverterFactory(convertFactory)
        addCallAdapterFactory(callAdapterFactory)
        build()
    }
}
