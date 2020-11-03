package br.com.programadorthi.facts.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.programadorthi.chucknorrisfacts.ext.viewModel
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.databinding.ActivityFactsBinding
import br.com.programadorthi.facts.di.factsModule
import br.com.programadorthi.facts.ui.component.ErrorComponent
import br.com.programadorthi.facts.ui.component.LoadingComponent
import br.com.programadorthi.facts.ui.component.SuccessComponent
import br.com.programadorthi.facts.ui.model.FactViewData
import br.com.programadorthi.facts.ui.viewmodel.FactsViewModel
import org.kodein.di.DI
import org.kodein.di.DIAware

class FactsActivity : AppCompatActivity(), DIAware {

    override val di: DI by DI.lazy {
        extend((application as DIAware).di)
        import(factsModule)
    }

    private val factsViewModel: FactsViewModel by viewModel()
    private lateinit var viewBinding: ActivityFactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFactsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        ErrorComponent(factsViewModel.facts, viewBinding.root)
        LoadingComponent(factsViewModel.facts, viewBinding.factsProgressBar)
        SuccessComponent(factsViewModel.facts, viewBinding.factsRecyclerView, ::shareFact) {
            Toast.makeText(
                this,
                R.string.activity_facts_empty_search_result,
                Toast.LENGTH_LONG
            ).show()
        }
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
