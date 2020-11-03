package br.com.programadorthi.facts.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.programadorthi.chucknorrisfacts.UIState
import br.com.programadorthi.chucknorrisfacts.ext.toStringRes
import br.com.programadorthi.domain.ResultTypes
import br.com.programadorthi.domain.getOrDefault
import br.com.programadorthi.domain.resource.StringProvider
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.facts.domain.FactsBusiness
import br.com.programadorthi.facts.domain.FactsUseCase
import br.com.programadorthi.facts.ui.model.FactViewData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import br.com.programadorthi.chucknorrisfacts.R as mainR
import com.google.android.material.R as materialR

class FactsViewModel(
    private val factsUseCase: FactsUseCase,
    private val stringProvider: StringProvider,
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
                    mutableFacts.emit(
                        UIState.Failed(
                            result,
                            stringProvider.getString(result.toStringRes())
                        )
                    )
                is ResultTypes.Error -> mutableFacts.emit(
                    UIState.Error(
                        result.cause,
                        stringProvider.getString(result.toStringRes())
                    )
                )
                else -> {
                    result
                        .getOrDefault(emptyList())
                        .map(::mapFact)
                        .let { facts -> mutableFacts.emit(UIState.Success(facts)) }
                }
            }
        }
    }

    private fun mapFact(fact: Fact) = FactViewData(
        category = fact.categories.firstOrNull()
            ?: stringProvider.getString(R.string.item_fact_view_holder_uncategorized_label),
        url = fact.url,
        value = fact.value,
        style = if (fact.value.length > HIGH_FONT_CHARACTERS_LIMIT) {
            materialR.style.TextAppearance_MaterialComponents_Subtitle1
        } else {
            materialR.style.TextAppearance_MaterialComponents_Headline4
        }
    )

    private fun ResultTypes.Business.toStringRes(): Int =
        when (this) {
            is FactsBusiness.EmptySearch -> R.string.activity_facts_empty_search_term
            else -> mainR.string.something_wrong
        }

    private companion object {
        private const val HIGH_FONT_CHARACTERS_LIMIT = 80
    }
}
