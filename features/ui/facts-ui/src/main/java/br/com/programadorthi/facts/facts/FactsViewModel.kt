package br.com.programadorthi.facts.facts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.facts.FactsUseCase
import br.com.programadorthi.facts.model.FactViewData
import io.reactivex.disposables.CompositeDisposable

class FactsViewModel(
    private val factsUseCase: FactsUseCase
) : ViewModel() {

    private val mutableFacts = MutableLiveData<List<FactViewData>>()
    val facts: LiveData<List<FactViewData>>
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
            .subscribe({ data ->
                mutableFacts.postValue(data)
            }, { err ->
                // TODO: catch errors
            })
        compositeDisposable.add(disposable)
    }
}
