package br.com.programadorthi.facts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.facts.R
import br.com.programadorthi.facts.model.FactViewData

class FactsAdapter(
    private val shareAction: (FactViewData) -> Unit
) : RecyclerView.Adapter<FactsViewHolder>() {

    private val dataSet = mutableListOf<FactViewData>()

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_fact, parent, false)
        val viewHolder = FactsViewHolder(view)

        view.setOnClickListener {
            val item = dataSet[viewHolder.adapterPosition]
            shareAction.invoke(item)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: FactsViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item)
    }

    fun changeData(facts: List<FactViewData>) {
        dataSet.clear()
        dataSet.addAll(facts)
        notifyDataSetChanged()
    }
}
