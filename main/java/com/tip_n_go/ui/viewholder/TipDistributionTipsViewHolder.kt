package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.databinding.ItemTipWithCheckboxBinding
import com.tip_n_go.ui.listener.CheckBoxListener

class TipDistributionTipsViewHolder(val binding: ItemTipWithCheckboxBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: Tip, isCheckAll: Boolean, listener: CheckBoxListener?) {
        val name = data.user?.fullName
        val date = data.date
        val amount = data.amountLocalized
        val tipRate = data.tipRate

        binding.tipOwner.text = name
        binding.tipDate.text = date
        binding.tipAmount.text = amount
        binding.tipRate.text = tipRate
        if (binding.checkbox.isChecked == isCheckAll) {
            binding.checkbox.isChecked = !isCheckAll
        }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            listener?.onCheck(adapterPosition, isChecked)
        }
        binding.checkbox.isChecked = isCheckAll
        binding.root.setOnClickListener {
            binding.checkbox.isChecked = !binding.checkbox.isChecked
        }
    }
}
