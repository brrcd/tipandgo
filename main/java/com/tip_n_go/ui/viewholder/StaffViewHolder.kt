package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.databinding.ItemStaffBinding
import com.tip_n_go.tools.GlideApp
import com.tip_n_go.tools.setRatingWithDrawableEnd
import com.tip_n_go.ui.listener.StaffListener

class StaffViewHolder(private val binding: ItemStaffBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(user: OrganizationUser, listener: StaffListener) {
        val title = user.fullName
        val role = user.position
        val avatar = user.avatar
        val rating = user.rating ?: 0.0

        binding.staffTitle.text = title
        binding.staffSubtitle.text = role
        binding.staffAverageRate.setRatingWithDrawableEnd(rating)

        GlideApp.with(binding.root.context)
            .load(avatar)
            .into(binding.avatarImage)

        binding.root.setOnClickListener {
            listener.onStaffSelected(adapterPosition)
        }
    }
}
