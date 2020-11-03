package br.com.programadorthi.facts.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.programadorthi.chucknorrisfacts.ext.viewModel
import br.com.programadorthi.facts.databinding.ActivitySearchFactsBinding
import br.com.programadorthi.facts.di.factsModule
import br.com.programadorthi.facts.ui.component.ChipsComponent
import br.com.programadorthi.facts.ui.component.SearchEditTextComponent
import br.com.programadorthi.facts.ui.viewmodel.SearchFactsViewModel
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

        SearchEditTextComponent(viewBinding.searchFactsEditText, ::goToFactsList)
        ChipsComponent(
            searchFactsViewModel.categories,
            viewBinding.suggestionsContainer,
            ::goToFactsList
        )
        ChipsComponent(
            searchFactsViewModel.lastSearches,
            viewBinding.lastSearchesContainer,
            ::goToFactsList
        )

        searchFactsViewModel.run {
            fetchCategories()
            fetchLastSearches()
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
