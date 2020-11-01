package br.com.programadorthi.facts.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import br.com.programadorthi.chucknorrisfacts.ext.viewModel
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.databinding.ActivitySearchFactsBinding
import br.com.programadorthi.facts.di.factsModule
import br.com.programadorthi.facts.ui.viewmodel.SearchFactsViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.collect
import org.kodein.di.DI
import org.kodein.di.DIAware

class SearchFactsActivity : AppCompatActivity(), DIAware {

    override val di: DI by DI.lazy {
        extend((application as DIAware).di)
        import(factsModule)
    }

    private val searchFactsViewModel: SearchFactsViewModel by viewModel()

    private lateinit var viewBinding: ActivitySearchFactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySearchFactsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        lifecycleScope.launchWhenCreated {
            searchFactsViewModel.categories.collect(::handleCategoriesResult)
            searchFactsViewModel.lastSearches.collect(::handleLastSearches)
        }

        setupSearchEditText()

        searchFactsViewModel.run {
            fetchCategories()
            fetchLastSearches()
        }
    }

    private fun handleCategoriesResult(result: UIState<List<String>>) {
        when (result) {
            is UIState.Success -> {
                viewBinding.searchFactsCategoriesTitleTextView.visibility = View.VISIBLE
                loadChips(result.data, viewBinding.searchFactsCategoriesChipGroup)
            }
            is UIState.Failed -> {
                viewBinding.searchFactsCategoriesTitleTextView.visibility = View.INVISIBLE
                loadChips(emptyList(), viewBinding.searchFactsCategoriesChipGroup)
            }
            else -> Unit
        }
    }

    private fun handleLastSearches(result: UIState<List<String>>) {
        if (result is UIState.Success) {
            loadChips(result.data, viewBinding.searchFactsLastSearchesChipGroup)
        }
        viewBinding.searchFactsLastSearchesTitleTextView.isVisible = result is UIState.Success
    }

    private fun loadChips(items: List<String>, view: ChipGroup) {
        view.apply {
            removeAllViews()

            if (items.isEmpty()) return@apply

            val inflater = LayoutInflater.from(context)

            for (category in items) {
                val chip = inflater.inflate(R.layout.item_search_fact_category, null)
                if (chip is Chip) {
                    chip.text = category
                    chip.setOnClickListener { goToFactsList(category) }
                    addView(chip)
                }
            }
        }
    }

    private fun setupSearchEditText() {
        viewBinding.searchFactsEditText.apply {
            setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    goToFactsList(view.text.toString())
                }

                return@setOnEditorActionListener false
            }
        }
    }

    private fun goToFactsList(query: String) {
        val intent = Intent().apply {
            putExtra(FactsActivity.SEARCH_RESULT_EXTRA_KEY, query)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}