package br.com.programadorthi.facts.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.domain.Result
import br.com.programadorthi.facts.domain.FactsUseCase
import br.com.programadorthi.facts.ui.model.FactViewData
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class FactsViewModel(
    private val scheduler: Scheduler,
    private val factsUseCase: FactsUseCase
) : ViewModel() {

    private val mutableFacts = MutableLiveData<Result<List<FactViewData>>>()
    val facts: LiveData<Result<List<FactViewData>>>
        get() = mutableFacts

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun search(text: String) {
        val disposable = factsUseCase.search(text)
            .subscribeOn(scheduler)
            .doOnSubscribe { mutableFacts.postValue(Result.Loading) }
            .toObservable()
            .flatMapIterable { items -> items }
            .map { fact ->
                FactViewData(
                    category = fact.categories.firstOrNull() ?: "",
                    url = fact.url,
                    value = fact.value
                )
            }
            .toList()
            .map<Result<List<FactViewData>>> { items -> Result.Success(items) }
            .onErrorReturn { err -> Result.Error(err) }
            .subscribe(mutableFacts::postValue)

        compositeDisposable.add(disposable)
    }
}
