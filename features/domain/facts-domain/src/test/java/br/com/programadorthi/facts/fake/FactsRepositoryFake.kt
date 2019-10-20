package br.com.programadorthi.facts.fake

import br.com.programadorthi.facts.Fact
import br.com.programadorthi.facts.FactsRepository
import io.reactivex.Single

class FactsRepositoryFake(
    var categories: List<String> = emptyList(),
    var termsAndResults: Map<String, List<Fact>> = emptyMap()
) : FactsRepository {

    override fun fetchCategories(offset: Int): Single<List<String>> {
        val result = if (offset > categories.size) {
            categories
        } else {
            categories.slice(IntRange(start = 0, endInclusive = offset - 1))
        }

        return Single.just(result)
    }

    override fun doSearch(text: String): Single<List<Fact>> =
        Single.just(termsAndResults[text] ?: emptyList())
}
