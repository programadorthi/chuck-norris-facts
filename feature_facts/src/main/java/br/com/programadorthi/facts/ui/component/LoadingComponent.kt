package br.com.programadorthi.facts.ui.component

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import br.com.programadorthi.chucknorrisfacts.UIState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoadingComponent(
    uiState: StateFlow<UIState<*>>,
    view: View
) {
    init {
        (view.context as? LifecycleOwner)?.lifecycleScope?.launch {
            uiState.collect { state ->
                view.isVisible = state is UIState.Loading
            }
        }
    }
}