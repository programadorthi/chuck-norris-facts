package br.com.programadorthi.facts.facts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.programadorthi.domain.Result
import br.com.programadorthi.facts.FactsBusiness
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.adapter.FactsAdapter
import br.com.programadorthi.facts.model.FactViewData
import br.com.programadorthi.facts.search.SearchFactsActivity
import kotlinx.android.synthetic.main.activity_facts.factsProgressBar
import kotlinx.android.synthetic.main.activity_facts.factsRecyclerView
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class FactsActivity : AppCompatActivity() {

    private val factsViewModel: FactsViewModel by currentScope.viewModel(this)

    private val factsAdapter = FactsAdapter(::shareFact)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facts)

        factsViewModel.facts.observe(this, Observer { result -> handleFacts(result) })

        factsRecyclerView.adapter = factsAdapter
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

    private fun handleFacts(result: Result<List<FactViewData>>) {
        when (result) {
            is Result.Error -> handleSearchError(result.cause)
            is Result.Success -> {
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
        }

        factsProgressBar.visibility = if (result is Result.Loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun handleSearchError(cause: Throwable) {
        val messageId = when (cause) {
            is FactsBusiness.EmptySearch -> R.string.activity_facts_empty_search_term
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
