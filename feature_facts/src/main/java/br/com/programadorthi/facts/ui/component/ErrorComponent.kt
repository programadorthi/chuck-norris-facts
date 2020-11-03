package br.com.programadorthi.facts.ui.component

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import br.com.programadorthi.chucknorrisfacts.ui.UIState
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
                val message = when (state) {
                    is UIState.Failed -> state.message
                    is UIState.Error -> state.message
                    else -> ""
                }
                if (message.isNotBlank()) {
                    Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
