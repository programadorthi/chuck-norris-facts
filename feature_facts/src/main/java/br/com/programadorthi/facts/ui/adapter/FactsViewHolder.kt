package br.com.programadorthi.facts.ui.adapter

import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.facts.databinding.ItemFactBinding
import br.com.programadorthi.facts.ui.model.FactViewData

class FactsViewHolder(
    private val viewBinding: ItemFactBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(fact: FactViewData) {
        TextViewCompat.setTextAppearance(viewBinding.itemFactContentTextView, fact.style)
        viewBinding.itemFactContentTextView.text = fact.value
        viewBinding.itemFactCategoryTextView.text = fact.category
    }

}
