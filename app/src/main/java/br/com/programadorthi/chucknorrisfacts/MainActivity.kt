package br.com.programadorthi.chucknorrisfacts

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.com.programadorthi.facts.facts.FactsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            startActivity(Intent(this, FactsActivity::class.java))
            finish()
        }, DELAY_SIMULATION)
    }

    private companion object {
        private const val DELAY_SIMULATION = 1000L
    }
}
