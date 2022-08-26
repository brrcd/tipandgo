package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.BankCard
import com.tip_n_go.databinding.ItemBankCardBinding
import com.tip_n_go.ui.viewholder.BankCardViewHolder

class BankCardAdapter : RecyclerView.Adapter<BankCardViewHolder>() {

    private var list = listOf<BankCard>()

    fun updateList(list: List<BankCard>) {
        this.list = list
        this.notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankCardViewHolder {
        val binding = ItemBankCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BankCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankCardViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount() = list.size
}
