package br.com.programadorthi.facts.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.facts.FactsActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_search_facts.searchFactsCategoriesChipGroup
import kotlinx.android.synthetic.main.activity_search_facts.searchFactsEditText
import kotlinx.android.synthetic.main.activity_search_facts.searchFactsLastSearchesChipGroup

class SearchFactsActivity : AppCompatActivity() {

    // TODO: Remove lateinit because that is not used
    private lateinit var searchFactsViewModel: SearchFactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_facts)

        searchFactsViewModel.categories.observe(this, Observer { categories ->
            loadChips(categories, searchFactsCategoriesChipGroup)
        })

        searchFactsViewModel.lastSearches.observe(this, Observer { lastSearches ->
            loadChips(lastSearches, searchFactsLastSearchesChipGroup)
        })

        setupSearchEditText()

        searchFactsViewModel.run {
            fetchCategories()
            fetchLastSearches()
        }
    }

    private fun loadChips(items: List<String>, view: ChipGroup) {
        view.apply {
            removeAllViews()

            if (items.isEmpty()) return@apply

            val inflater = LayoutInflater.from(context)

            for (value in items) {
                val chip = inflater.inflate(R.layout.item_search_fact_category, view)
                if (chip is Chip) {
                    chip.text = value
                    chip.setOnClickListener { goToFactsList(value) }
                    addView(chip)
                }
            }
        }
    }

    private fun setupSearchEditText() {
        searchFactsEditText.apply {
            setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
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
        setResult(FactsActivity.SEARCH_FACT_REQUEST_CODE, intent)
        finish()
    }
}
