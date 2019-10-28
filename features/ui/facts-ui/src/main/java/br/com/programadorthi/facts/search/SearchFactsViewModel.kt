package br.com.programadorthi.facts.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.domain.Result
import br.com.programadorthi.facts.FactsUseCase
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class SearchFactsViewModel(
    private val scheduler: Scheduler,
    private val factsUseCase: FactsUseCase
) : ViewModel() {

    private val mutableCategories = MutableLiveData<Result<List<String>>>()
    val categories: LiveData<Result<List<String>>>
        get() = mutableCategories

    private val mutableLastSearches = MutableLiveData<List<String>>()
    val lastSearches: LiveData<List<String>>
        get() = mutableLastSearches

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun fetchCategories() {
        val disposable = factsUseCase
            .categories(limit = MAX_VISIBLE_CATEGORIES, shuffle = true)
            .subscribeOn(scheduler)
            .map<Result<List<String>>> { cats -> Result.Success(cats) }
            .onErrorReturn { err -> Result.Error(err) }
            .subscribe(mutableCategories::postValue)
        compositeDisposable.add(disposable)
    }

    fun fetchLastSearches() {
        val disposable = factsUseCase
            .lastSearches()
            .subscribeOn(scheduler)
            .subscribe(mutableLastSearches::postValue)
        compositeDisposable.add(disposable)
    }

    private companion object {
        private const val MAX_VISIBLE_CATEGORIES = 8
    }
}
