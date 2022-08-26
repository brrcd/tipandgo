package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Organization
import com.tip_n_go.databinding.ItemOrganizationBinding
import com.tip_n_go.tools.GlideApp
import com.tip_n_go.tools.setRatingWithDrawableEnd
import com.tip_n_go.ui.listener.OrganizationListener

class OrganizationsViewHolder(private val binding: ItemOrganizationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(organization: Organization, listener: OrganizationListener?) {
        val title = organization.brand?.name
        val subtitle = organization.name
        val avatar = organization.brand?.logo
        val averageRate = organization.rating ?: 0.0

        binding.organizationTitle.text = title
        binding.organizationSubtitle.text = subtitle
        binding.organizationAverageRate.setRatingWithDrawableEnd(averageRate)
        binding.root.setOnClickListener {
            listener?.onOrganizationSelected(adapterPosition)
        }

        GlideApp.with(binding.root.context)
            .load(avatar)
            .into(binding.avatarImage)
    }
}
