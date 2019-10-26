package br.com.programadorthi.facts.adapter

import android.view.View
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.model.FactViewData
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_fact.itemFactCategoryTextView
import kotlinx.android.synthetic.main.item_fact.itemFactContentTextView
import com.google.android.material.R as materialR

class FactsViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(fact: FactViewData) {
        setupFactContent(fact)
        setupFactCategory(fact)
    }

    private fun setupFactContent(fact: FactViewData) {
        val factStyle = if (fact.applySmallFont()) {
            materialR.style.TextAppearance_MaterialComponents_Subtitle1
        } else {
            materialR.style.TextAppearance_MaterialComponents_Headline4
        }

        TextViewCompat.setTextAppearance(itemFactContentTextView, factStyle)
        itemFactContentTextView.text = fact.value
    }

    private fun setupFactCategory(fact: FactViewData) {
        itemFactCategoryTextView.text = if (fact.category.isNotBlank()) {
            fact.category
        } else {
            containerView.context.getString(R.string.item_fact_view_holder_uncategorized_label)
        }
    }
}
