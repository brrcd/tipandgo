package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.databinding.ItemStaffBinding
import com.tip_n_go.ui.listener.StaffListener
import com.tip_n_go.ui.viewholder.StaffViewHolder

class StaffAdapter(private val listener: StaffListener) : RecyclerView.Adapter<StaffViewHolder>() {

    private var list = listOf<OrganizationUser>()

    fun updateList(list: List<OrganizationUser>) {
        this.list = list
        notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val binding =
            ItemStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        holder.bindData(list[position], listener)
    }

    override fun getItemCount() = list.size
}
