package br.com.programadorthi.facts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.programadorthi.facts.adapter.FactsAdapter
import br.com.programadorthi.facts.model.FactViewData

class FactsActivity : AppCompatActivity() {

    // TODO: Remove lateinit because that is not used
    private lateinit var factsViewModel: FactsViewModel

    private val factsAdapter = FactsAdapter(::shareFact)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facts)

        factsViewModel.facts.observe(this, Observer { facts -> handleFacts(facts) })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chuck_norris_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSearch) {
            // TODO: search
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleFacts(facts: List<FactViewData>) {
        // TODO: Catch Results instead of list
        factsAdapter.changeData(facts)
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

    private companion object {
        private const val SHARE_FACT_CONTENT_TYPE = "text/plain"
    }
}
