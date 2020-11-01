package br.com.programadorthi.facts.ui.adapter

import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.databinding.ItemFactBinding
import br.com.programadorthi.facts.ui.model.FactViewData
import com.google.android.material.R as materialR

class FactsViewHolder(
    private val viewBinding: ItemFactBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

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

        TextViewCompat.setTextAppearance(viewBinding.itemFactContentTextView, factStyle)
        viewBinding.itemFactContentTextView.text = fact.value
    }

    private fun setupFactCategory(fact: FactViewData) {
        viewBinding.itemFactCategoryTextView.text = if (fact.category.isNotBlank()) {
            fact.category
        } else {
            viewBinding.root.context.getString(R.string.item_fact_view_holder_uncategorized_label)
        }
    }
}
