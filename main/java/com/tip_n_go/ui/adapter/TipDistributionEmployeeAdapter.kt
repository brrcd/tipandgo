package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.databinding.ItemEmployeeWithCheckboxBinding
import com.tip_n_go.ui.listener.CheckBoxListener
import com.tip_n_go.ui.viewholder.TipDistributionEmployeeViewHolder

class TipDistributionEmployeeAdapter : RecyclerView.Adapter<TipDistributionEmployeeViewHolder>() {

    private var list: List<OrganizationUser> = listOf()
    private var isCheckAll: Boolean = false
    private var listener: CheckBoxListener? = null

    fun updateList(list: List<OrganizationUser>) {
        this.list = list
        notifyItemRangeInserted(0, list.size)
    }

    fun checkAll(isCheckAll: Boolean) {
        this.isCheckAll = isCheckAll
        notifyItemRangeChanged(0, list.size)
    }

    fun setListener(listener: CheckBoxListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TipDistributionEmployeeViewHolder {
        val binding = ItemEmployeeWithCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TipDistributionEmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipDistributionEmployeeViewHolder, position: Int) {
        holder.bindData(list[position], isCheckAll, listener)
    }

    override fun getItemCount() = list.size
}
