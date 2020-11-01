package br.com.programadorthi.facts.data.remote

import br.com.programadorthi.facts.data.remote.raw.FactsResponseRaw
import retrofit2.http.GET
import retrofit2.http.Query

interface FactsService {
    @GET("categories")
    suspend fun fetchCategories(): List<String>

    @GET("search")
    suspend fun search(@Query("query") query: String): FactsResponseRaw
}
