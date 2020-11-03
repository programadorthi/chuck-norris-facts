package br.com.programadorthi.facts.ui.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import br.com.programadorthi.chucknorrisfacts.ui.UIState
import br.com.programadorthi.facts.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChipsComponent(
    uiState: StateFlow<UIState<List<String>>>,
    private val view: ViewGroup,
    private val onChipClicked: (String) -> Unit
) {
    init {
        (view.context as? LifecycleOwner)?.lifecycleScope?.launch {
            uiState.collect { state ->
                val chips = when (state) {
                    is UIState.Success -> state.data
                    else -> emptyList()
                }
                loadChips(chips)
            }
        }
    }

    private fun loadChips(items: List<String>) {
        view.isVisible = items.isNotEmpty()
        val child = view.children.firstOrNull { it is ChipGroup } ?: return
        (child as ChipGroup).apply {
            removeAllViews()
            val inflater = LayoutInflater.from(context)
            for (category in items) {
                val chip = inflater.inflate(R.layout.item_search_fact_category, null)
                if (chip is Chip) {
                    chip.text = category
                    chip.setOnClickListener { onChipClicked(category) }
                    addView(chip)
                }
            }
        }
    }
}