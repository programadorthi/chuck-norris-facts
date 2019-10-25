package br.com.programadorthi.facts.facts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.domain.Result
import br.com.programadorthi.facts.FactsUseCase
import br.com.programadorthi.facts.model.FactViewData
import io.reactivex.disposables.CompositeDisposable

class FactsViewModel(
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
            .doOnSubscribe { mutableFacts.postValue(Result.Loading) }
            .subscribe(mutableFacts::postValue)
        compositeDisposable.add(disposable)
    }
}
