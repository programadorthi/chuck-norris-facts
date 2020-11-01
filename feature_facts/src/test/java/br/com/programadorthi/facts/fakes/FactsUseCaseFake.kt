package br.com.programadorthi.facts.fakes

import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.facts.domain.FactsUseCase
import io.reactivex.Single

class FactsUseCaseFake(
    var categories: List<String> = emptyList(),
    var lastSearches: List<String> = emptyList(),
    var searchResult: List<Fact> = emptyList(),
    var categoriesException: Throwable? = null,
    var searchException: Throwable? = null
) : FactsUseCase {
    override fun categories(limit: Int, shuffle: Boolean): Single<List<String>> {
        if (categoriesException != null) {
            return Single.error(categoriesException)
        }
        val result = when {
            limit <= 0 -> emptyList()
            limit > categories.size -> categories
            else -> categories.slice(IntRange(0, limit - 1))
        }
        return Single.just(result)
    }

    override fun lastSearches(): Single<List<String>> =
        Single.just(lastSearches)

    override fun search(text: String): Single<List<Fact>> {
        if (searchException != null) {
            return Single.error(searchException)
        }
        return Single.just(searchResult)
    }
}
