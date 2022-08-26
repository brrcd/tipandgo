package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.BankCard
import com.tip_n_go.databinding.ItemBankCardBinding

class BankCardViewHolder(private val binding: ItemBankCardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(bankCard: BankCard) {
        val number = bankCard.cardNumberMasked
        val expireDate = "${bankCard.expMonth}/${bankCard.expYear}"

        binding.cardNumber.text = number
        binding.cardExpireDate.text = expireDate
    }
}
