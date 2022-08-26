package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.databinding.ItemTipBinding
import com.tip_n_go.ui.viewholder.TipsViewHolder

class TipsAdapter : RecyclerView.Adapter<TipsViewHolder>() {

    private var list = listOf<Tip>()

    fun updateList(list: List<Tip>) {
        this.list = list
        this.notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        val binding = ItemTipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
