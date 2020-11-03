package br.com.programadorthi.facts.ui.component

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import br.com.programadorthi.chucknorrisfacts.UIState
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.domain.FactsBusiness
import br.com.programadorthi.network.exception.NetworkingError
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ErrorComponent(
    uiState: StateFlow<UIState<*>>,
    view: View
) {
    init {
        (view.context as? LifecycleOwner)?.lifecycleScope?.launch {
            uiState.collect { state ->
                val messageId = when (state) {
                    is UIState.Failed -> mapFailedState(state)
                    is UIState.Error -> mapErrorState(state)
                    else -> -1
                }
                if (messageId >= 0) {
                    Toast.makeText(view.context, messageId, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun mapErrorState(state: UIState.Error): Int =
        when (state.cause) {
            is NetworkingError.NoInternetConnection ->
                R.string.activity_facts_no_internet_connection
            else -> R.string.activity_facts_something_wrong
        }

    private fun mapFailedState(state: UIState.Failed): Int =
        when (state.cause) {
            is FactsBusiness.EmptySearch -> R.string.activity_facts_empty_search_term
            else -> R.string.activity_facts_something_wrong
        }
}