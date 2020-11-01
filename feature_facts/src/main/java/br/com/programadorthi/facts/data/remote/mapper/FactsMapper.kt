package br.com.programadorthi.facts.data.remote.mapper

import br.com.programadorthi.facts.data.remote.raw.FactRaw
import br.com.programadorthi.facts.data.remote.raw.FactsResponseRaw
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.network.mapper.RemoteMapper

internal class FactsMapper : RemoteMapper<FactsResponseRaw, List<Fact>>() {

    override fun checkEssentialParams(missingFields: MutableList<String>, raw: FactsResponseRaw) {
        if (raw.result == null) {
            missingFields.add(FactsResponseRaw.RESULT_FIELD)
        }

        val facts = raw.result ?: return
        if (facts.isNotEmpty()) {
            val first = facts.first()
            when {
                first.id == null -> missingFields.add(FactRaw.ID_FIELD)
                first.url == null -> missingFields.add(FactRaw.URL_FIELD)
                first.value == null -> missingFields.add(FactRaw.VALUE_FIELD)
            }
        }
    }

    override fun copyValuesAfterEssentialParamsChecked(raw: FactsResponseRaw): List<Fact> {
        val items = raw.result ?: return emptyList()
        return items.map {
            Fact(
                id = it.id ?: "",
                url = it.url ?: "",
                value = it.value ?: "",
                categories = it.categories ?: emptyList()
            )
        }
    }
}
