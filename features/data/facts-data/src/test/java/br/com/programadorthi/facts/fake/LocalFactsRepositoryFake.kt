package br.com.programadorthi.facts.fake

import br.com.programadorthi.facts.local.LocalFactsRepository
import io.reactivex.Completable
import io.reactivex.Single

class LocalFactsRepositoryFake(
    var categories: List<String> = emptyList(),
    var lastSearches: List<String> = emptyList(),
    var saveCategoriesError: Throwable? = null,
    var saveNewSearchError: Throwable? = null
) : LocalFactsRepository {
    override fun getCategories(): Single<List<String>> = Single.just(categories)

    override fun getLastSearches(): Single<List<String>> = Single.just(lastSearches)

    override fun saveCategories(categories: List<String>): Completable {
        if (saveCategoriesError != null) {
            return Completable.error(saveCategoriesError)
        }
        this.categories = categories
        return Completable.complete()
    }

    override fun saveNewSearch(text: String): Completable {
        if (saveNewSearchError != null) {
            return Completable.error(saveNewSearchError)
        }
        lastSearches = lastSearches.toMutableList().apply {
            add(text)
        }
        return Completable.complete()
    }
}
