package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.DistributedTip
import com.tip_n_go.databinding.ItemDistributedTipBinding
import com.tip_n_go.ui.listener.TipDestinationListener

class DistributedTipsViewHolder(private val binding: ItemDistributedTipBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(item: DistributedTip, listener: TipDestinationListener) {
        val distributedBy = item.user?.fullName
        val distributionDate = item.date
        val amount = item.amountLocalized
        val employeeCount = item.destinations?.size

        binding.distributedBy.text = distributedBy
        binding.distributionDate.text = distributionDate
        binding.distributionAmount.text = amount
        binding.employeeCount.text = employeeCount.toString()

        binding.root.setOnClickListener {
            listener.onTipDestinationSelected(adapterPosition)
        }
    }
}
