package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.TipDestination
import com.tip_n_go.databinding.ItemTipDestinationBinding
import com.tip_n_go.ui.viewholder.TipDestinationsViewHolder

class TipDestinationsAdapter : RecyclerView.Adapter<TipDestinationsViewHolder>() {

    private var list: List<TipDestination> = listOf()

    fun updateList(list: List<TipDestination>) {
        this.list = list
        this.notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipDestinationsViewHolder {
        val binding =
            ItemTipDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipDestinationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipDestinationsViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount() = list.size
}
