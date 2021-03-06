package br.com.programadorthi.facts.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.programadorthi.chucknorrisfacts.ui.UIState
import br.com.programadorthi.chucknorrisfacts.ext.toStringRes
import br.com.programadorthi.domain.ResultTypes
import br.com.programadorthi.domain.getOrDefault
import br.com.programadorthi.domain.resource.StringProvider
import br.com.programadorthi.facts.domain.FactsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import br.com.programadorthi.chucknorrisfacts.R as mainR

class SearchFactsViewModel(
    private val factsUseCase: FactsUseCase,
    private val stringProvider: StringProvider,
    private val ioScope: CoroutineScope
) : ViewModel(), CoroutineScope by ioScope {

    private val mutableCategories = MutableStateFlow<UIState<List<String>>>(UIState.Idle)
    val categories = mutableCategories.asStateFlow()

    private val mutableLastSearches = MutableStateFlow<UIState<List<String>>>(UIState.Idle)
    val lastSearches = mutableLastSearches.asStateFlow()

    fun fetchCategories() {
        launch {
            when (val result =
                factsUseCase.categories(limit = MAX_VISIBLE_CATEGORIES, shuffle = true)) {
                is ResultTypes.Business ->
                    mutableCategories.emit(
                        UIState.Failed(
                            result,
                            stringProvider.getString(mainR.string.empty_text)
                        )
                    )
                is ResultTypes.Error ->
                    mutableCategories.emit(
                        UIState.Error(
                            result.cause,
                            stringProvider.getString(result.toStringRes())
                        )
                    )
                else -> {
                    result
                        .getOrDefault(emptyList())
                        .let { categories -> mutableCategories.emit(UIState.Success(categories)) }
                }
            }
        }
    }

    fun fetchLastSearches() {
        launch {
            when (val result = factsUseCase.lastSearches()) {
                is ResultTypes.Error ->
                    mutableLastSearches.emit(
                        UIState.Error(
                            result.cause,
                            stringProvider.getString(result.toStringRes())
                        )
                    )
                else -> {
                    result
                        .getOrDefault(emptyList())
                        .let { searches -> mutableLastSearches.emit(UIState.Success(searches)) }
                }
            }
        }
    }

    private companion object {
        private const val MAX_VISIBLE_CATEGORIES = 8
    }
}
