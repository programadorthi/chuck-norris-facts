package br.com.programadorthi.facts.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FactsResponseRaw(
    @SerialName(RESULT_FIELD)
    val result: List<FactRaw>? = null
) {
    companion object {
        const val RESULT_FIELD = "result"
    }
}
