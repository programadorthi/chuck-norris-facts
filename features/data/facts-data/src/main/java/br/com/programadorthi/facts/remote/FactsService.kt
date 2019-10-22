package br.com.programadorthi.facts.remote

import br.com.programadorthi.facts.remote.raw.FactsResponseRaw
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FactsService {

    @GET("categories")
    fun fetchCategories(): Single<List<String>>

    @GET("search")
    fun search(@Query("query") query: String): Single<FactsResponseRaw>

}
