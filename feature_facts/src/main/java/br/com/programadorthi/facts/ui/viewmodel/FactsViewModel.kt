package br.com.programadorthi.facts.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.programadorthi.domain.ResultTypes
import br.com.programadorthi.domain.exceptionOrNull
import br.com.programadorthi.domain.getOrDefault
import br.com.programadorthi.facts.domain.FactsUseCase
import br.com.programadorthi.facts.ui.UIState
import br.com.programadorthi.facts.ui.model.FactViewData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FactsViewModel(
    private val factsUseCase: FactsUseCase,
    private val ioScope: CoroutineScope
) : ViewModel(), CoroutineScope by ioScope {

    private val mutableFacts = MutableStateFlow<UIState<List<FactViewData>>>(UIState.Idle)
    val facts: StateFlow<UIState<List<FactViewData>>>
        get() = mutableFacts

    fun search(text: String) {
        launch {
            mutableFacts.emit(UIState.Loading)
            when (val result = factsUseCase.search(text)) {
                is ResultTypes.Business ->
                    mutableFacts.emit(UIState.Failed(result.exceptionOrNull()))
                is ResultTypes.Error -> mutableFacts.emit(UIState.Failed(result.exceptionOrNull()))
                else -> {
                    result
                        .getOrDefault(emptyList())
                        .map { fact ->
                            FactViewData(
                                category = fact.categories.firstOrNull() ?: "",
                                url = fact.url,
                                value = fact.value
                            )
                        }
                        .let { facts -> mutableFacts.emit(UIState.Success(facts)) }
                }
            }
        }
    }
}
