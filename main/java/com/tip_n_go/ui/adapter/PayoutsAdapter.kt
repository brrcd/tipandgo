package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Payout
import com.tip_n_go.databinding.ItemPayoutHistoryBinding
import com.tip_n_go.ui.viewholder.PayoutViewHolder

class PayoutsAdapter : RecyclerView.Adapter<PayoutViewHolder>() {

    private var list = listOf<Payout>()

    fun updateList(list: List<Payout>) {
        this.list = list
        this.notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayoutViewHolder {
        val binding =
            ItemPayoutHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PayoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PayoutViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount() = list.size
}
