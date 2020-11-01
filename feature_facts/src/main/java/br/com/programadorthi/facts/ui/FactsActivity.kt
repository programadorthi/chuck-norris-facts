package br.com.programadorthi.facts.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import br.com.programadorthi.chucknorrisfacts.ext.viewModel
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.databinding.ActivityFactsBinding
import br.com.programadorthi.facts.di.factsModule
import br.com.programadorthi.facts.ui.adapter.FactsAdapter
import br.com.programadorthi.facts.ui.model.FactViewData
import br.com.programadorthi.facts.ui.viewmodel.FactsViewModel
import br.com.programadorthi.network.exception.NetworkingError
import kotlinx.coroutines.flow.collect
import org.kodein.di.DI
import org.kodein.di.DIAware

class FactsActivity : AppCompatActivity(), DIAware {

    override val di: DI by DI.lazy {
        extend((application as DIAware).di)
        import(factsModule)
    }

    private val factsViewModel: FactsViewModel by viewModel()
    private val factsAdapter = FactsAdapter(::shareFact)

    private lateinit var viewBinding: ActivityFactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFactsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        lifecycleScope.launchWhenCreated {
            factsViewModel.facts.collect(::handleFacts)
        }

        viewBinding.factsRecyclerView.adapter = factsAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chuck_norris_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSearch) {
            startSearch()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SEARCH_FACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val query = data?.getStringExtra(SEARCH_RESULT_EXTRA_KEY) ?: EMPTY_TEXT
            factsViewModel.search(query)
        }
    }

    private fun handleFacts(result: UIState<List<FactViewData>>) {
        when (result) {
            is UIState.Failed -> handleSearchError(result.cause!!)
            is UIState.Success -> {
                if (result.data.isEmpty()) {
                    Toast.makeText(
                        this,
                        R.string.activity_facts_empty_search_result,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    factsAdapter.changeData(result.data)
                }
            }
            else -> Unit
        }

        viewBinding.factsProgressBar.isVisible = result is UIState.Loading
    }

    private fun handleSearchError(cause: Throwable) {
        val messageId = when (cause) {
            // FIXME: handle business
            // is FactsBusiness.EmptySearch -> R.string.activity_facts_empty_search_term
            is NetworkingError.NoInternetConnection ->
                R.string.activity_facts_no_internet_connection
            else -> R.string.activity_facts_something_wrong
        }
        Toast.makeText(this, messageId, Toast.LENGTH_LONG).show()
    }

    private fun shareFact(factViewData: FactViewData) {
        val shareFactIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, factViewData.url)
            type = SHARE_FACT_CONTENT_TYPE
        }

        startActivity(
            Intent.createChooser(shareFactIntent, "Share this fact")
        )
    }

    private fun startSearch() {
        val intent = Intent(this, SearchFactsActivity::class.java)
        startActivityForResult(intent, SEARCH_FACT_REQUEST_CODE)
    }

    companion object {
        private const val EMPTY_TEXT = ""
        private const val SHARE_FACT_CONTENT_TYPE = "text/plain"
        private const val SEARCH_FACT_REQUEST_CODE = 999

        const val SEARCH_RESULT_EXTRA_KEY = "search_result"
    }
}
