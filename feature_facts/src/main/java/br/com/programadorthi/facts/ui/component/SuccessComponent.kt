package br.com.programadorthi.facts.ui.component

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.chucknorrisfacts.UIState
import br.com.programadorthi.facts.ui.adapter.FactsAdapter
import br.com.programadorthi.facts.ui.model.FactViewData
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SuccessComponent(
    uiState: StateFlow<UIState<List<FactViewData>>>,
    view: RecyclerView,
    shareFact: (FactViewData) -> Unit,
    onEmptyDataSet: () -> Unit
) {
    private val factsAdapter = FactsAdapter(shareFact)

    init {
        view.adapter = factsAdapter
        (view.context as? LifecycleOwner)?.lifecycleScope?.launch {
            uiState.collect { state ->
                when (state) {
                    is UIState.Loading -> factsAdapter.changeData(emptyList())
                    is UIState.Success -> {
                        factsAdapter.changeData(state.data)
                        if (state.data.isEmpty()) {
                            onEmptyDataSet.invoke()
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}