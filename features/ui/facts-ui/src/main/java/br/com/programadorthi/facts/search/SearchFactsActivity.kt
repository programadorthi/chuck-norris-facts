package br.com.programadorthi.facts.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.programadorthi.domain.Result
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.facts.FactsActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_search_facts.searchFactsCategoriesChipGroup
import kotlinx.android.synthetic.main.activity_search_facts.searchFactsCategoriesTitleTextView
import kotlinx.android.synthetic.main.activity_search_facts.searchFactsEditText
import kotlinx.android.synthetic.main.activity_search_facts.searchFactsLastSearchesChipGroup
import kotlinx.android.synthetic.main.activity_search_facts.searchFactsLastSearchesTitleTextView
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFactsActivity : AppCompatActivity() {

    private val searchFactsViewModel: SearchFactsViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_facts)

        searchFactsViewModel.categories.observe(this, Observer { result ->
            handleCategoriesResult(result)
        })

        searchFactsViewModel.lastSearches.observe(this, Observer { lastSearches ->
            handleLastSearches(lastSearches)
        })

        setupSearchEditText()

        searchFactsViewModel.run {
            fetchCategories()
            fetchLastSearches()
        }
    }

    private fun handleCategoriesResult(result: Result<List<String>>?) {
        when (result) {
            is Result.Success -> {
                searchFactsCategoriesTitleTextView.visibility = View.VISIBLE
                loadChips(result.data, searchFactsCategoriesChipGroup)
            }
            is Result.Error -> {
                searchFactsCategoriesTitleTextView.visibility = View.INVISIBLE
                loadChips(emptyList(), searchFactsCategoriesChipGroup)
            }
        }
    }

    private fun handleLastSearches(lastSearches: List<String>) {
        searchFactsLastSearchesTitleTextView.visibility = if (lastSearches.isEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        loadChips(lastSearches, searchFactsLastSearchesChipGroup)
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
        searchFactsEditText.apply {
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
