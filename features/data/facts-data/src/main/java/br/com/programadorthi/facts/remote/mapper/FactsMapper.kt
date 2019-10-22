package br.com.programadorthi.facts.remote.mapper

import br.com.programadorthi.facts.Fact
import br.com.programadorthi.facts.remote.raw.FactRaw
import br.com.programadorthi.facts.remote.raw.FactsResponseRaw
import br.com.programadorthi.network.mapper.RemoteMapper

class FactsMapper : RemoteMapper<FactsResponseRaw, List<Fact>>() {

    override fun checkEssentialParams(missingFields: MutableList<String>, raw: FactsResponseRaw) {
        if (raw.result == null) {
            missingFields.add(FactsResponseRaw.RESULT_FIELD)
        }

        val first = raw.result?.first() ?: return
        when {
            first.id == null -> missingFields.add(FactRaw.ID_FIELD)
            first.url == null -> missingFields.add(FactRaw.URL_FIELD)
            first.value == null -> missingFields.add(FactRaw.VALUE_FIELD)
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
