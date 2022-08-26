package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Organization
import com.tip_n_go.databinding.ItemOrganizationBinding
import com.tip_n_go.ui.listener.OrganizationListener
import com.tip_n_go.ui.viewholder.OrganizationsViewHolder

class OrganizationsAdapter : RecyclerView.Adapter<OrganizationsViewHolder>() {

    private var list = listOf<Organization>()
    private var listener: OrganizationListener? = null

    fun updateList(list: List<Organization>) {
        this.list = list
        this.notifyItemRangeInserted(0, list.size)
    }

    fun setListener(listener: OrganizationListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationsViewHolder {
        val binding =
            ItemOrganizationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrganizationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrganizationsViewHolder, position: Int) {
        holder.bindData(list[position], listener)
    }

    override fun getItemCount() = list.size
}
