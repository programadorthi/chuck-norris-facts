package br.com.programadorthi.facts.fake

import br.com.programadorthi.facts.Fact
import br.com.programadorthi.facts.remote.repository.RemoteFactsRepository
import io.reactivex.Single

class RemoteFactsRepositoryFake(
    var categories: List<String> = emptyList(),
    var facts: List<Fact> = emptyList(),
    var fetchCategoriesError: Throwable? = null,
    var searchError: Throwable? = null
) : RemoteFactsRepository {
    override fun fetchCategories(): Single<List<String>> {
        if (fetchCategoriesError != null) {
            return Single.error(fetchCategoriesError)
        }
        return Single.just(categories)
    }

    override fun search(text: String): Single<List<Fact>> {
        if (searchError != null) {
            return Single.error(searchError)
        }
        return Single.just(facts)
    }
}
