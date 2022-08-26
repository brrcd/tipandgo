package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.databinding.ItemEmployeeWithCheckboxBinding
import com.tip_n_go.tools.GlideApp
import com.tip_n_go.ui.listener.CheckBoxListener

class TipDistributionEmployeeViewHolder(private val binding: ItemEmployeeWithCheckboxBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: OrganizationUser, isCheckAll: Boolean, listener: CheckBoxListener?) {
        val title = data.fullName
        val subtitle = data.position
        val avatar = data.avatar

        binding.title.text = title
        binding.subtitle.text = subtitle
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

        GlideApp.with(binding.root)
            .load(avatar)
            .into(binding.avatarImage)
    }
}
