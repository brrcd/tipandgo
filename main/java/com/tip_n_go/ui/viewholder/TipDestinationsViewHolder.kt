package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.TipDestination
import com.tip_n_go.databinding.ItemTipDestinationBinding
import com.tip_n_go.tools.GlideApp

class TipDestinationsViewHolder(private val binding: ItemTipDestinationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(item: TipDestination) {
        val title = item.user?.fullName
        val subtitle = item.user?.position
        val amount = item.amountLocalized
        val avatar = item.user?.avatar

        binding.title.text = title
        binding.subtitle.text = subtitle
        binding.tipAmount.text = amount

        GlideApp.with(binding.root.context)
            .load(avatar)
            .into(binding.avatarImage)
    }
}
