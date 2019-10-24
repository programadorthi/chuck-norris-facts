package br.com.programadorthi.facts.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.facts.FactsUseCase
import io.reactivex.disposables.CompositeDisposable

class SearchFactsViewModel(
    private val factsUseCase: FactsUseCase
) : ViewModel() {

    private val mutableCategories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = mutableCategories

    private val mutableLastSearches = MutableLiveData<List<String>>()
    val lastSearches: LiveData<List<String>>
        get() = mutableLastSearches

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun fetchCategories() {
        // TODO: Remove offset and apply shuffled list
        val disposable = factsUseCase.categories(MAX_VISIBLE_CATEGORIES)
            .toObservable()
            .flatMapIterable { items -> items }
            .take(MAX_VISIBLE_CATEGORIES.toLong())
            .toList()
            .subscribe({ data ->
                mutableCategories.postValue(data)
            }, { err ->
                // TODO: catch errors
            })
        compositeDisposable.add(disposable)
    }

    fun fetchLastSearches() {
        val disposable = factsUseCase.lastSearches()
            .toObservable()
            .flatMapIterable { items -> items }
            .toList()
            .subscribe({ data ->
                mutableLastSearches.postValue(data)
            }, { err ->
                // TODO: catch errors
            })
        compositeDisposable.add(disposable)
    }

    private companion object {
        private const val MAX_VISIBLE_CATEGORIES = 8
    }
}
