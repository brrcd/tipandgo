package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Option
import com.tip_n_go.databinding.ItemOptionBinding
import com.tip_n_go.ui.listener.OptionListener

class OptionsViewHolder(val binding: ItemOptionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: Option, listener: OptionListener?) {
        val title = data.name
        val subtitle = data.description
        val isEnabled = data.checked ?: false
        val id = data.id ?: -1

        binding.title.text = title
        binding.subtitle.text = subtitle
        binding.checkbox.isChecked = isEnabled
        binding.root.setOnClickListener {
            if (!binding.checkbox.isChecked) {
                binding.checkbox.isChecked = !binding.checkbox.isChecked
                listener?.onOptionSelected(adapterPosition, id, binding.checkbox.isChecked)
            }
        }
        listener?.onOptionViewCreated(binding.checkbox)
    }
}
