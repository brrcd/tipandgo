package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.DistributedTip
import com.tip_n_go.databinding.ItemDistributedTipBinding
import com.tip_n_go.ui.listener.TipDestinationListener
import com.tip_n_go.ui.viewholder.DistributedTipsViewHolder

class DistributedTipsAdapter(private val listener: TipDestinationListener) :
    RecyclerView.Adapter<DistributedTipsViewHolder>() {

    private var list: List<DistributedTip> = listOf()

    fun updateList(list: List<DistributedTip>) {
        this.list = list
        this.notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistributedTipsViewHolder {
        val binding =
            ItemDistributedTipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DistributedTipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DistributedTipsViewHolder, position: Int) {
        holder.bindData(list[position], listener)
    }

    override fun getItemCount() = list.size
}
