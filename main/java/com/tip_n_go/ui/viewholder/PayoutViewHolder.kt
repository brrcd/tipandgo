package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Payout
import com.tip_n_go.databinding.ItemPayoutHistoryBinding

class PayoutViewHolder(private val binding: ItemPayoutHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(payout: Payout) {
        val cardNumber = payout.userBankCard?.cardNumberMasked
        val payoutDate = payout.date
        val payoutAmount = payout.amountLocalized

        binding.payoutCardNumber.text = cardNumber
        binding.payoutDate.text = payoutDate
        binding.payoutAmount.text = payoutAmount
    }
}
