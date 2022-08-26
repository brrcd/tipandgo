package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.databinding.ItemTipBinding

class TipsViewHolder(private val binding: ItemTipBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(tip: Tip) = with(binding) {
        val date = tip.date
        val amount = tip.amountLocalized
        val firstName = tip.user?.firstName
        val rate = tip.tipRate.toString()

        tipDate.text = date
        tipAmount.text = amount
        tipOwner.text = firstName
        tipRate.text = rate
    }
}
