package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.databinding.ItemTipWithCheckboxBinding
import com.tip_n_go.ui.listener.CheckBoxListener
import com.tip_n_go.ui.viewholder.TipDistributionTipsViewHolder

class TipDistributionTipsAdapter : RecyclerView.Adapter<TipDistributionTipsViewHolder>() {

    private var list = listOf<Tip>()
    private var isCheckAll: Boolean = false
    private var listener: CheckBoxListener? = null

    fun updateList(list: List<Tip>) {
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
    ): TipDistributionTipsViewHolder {
        val binding =
            ItemTipWithCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipDistributionTipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipDistributionTipsViewHolder, position: Int) {
        holder.bindData(list[position], isCheckAll, listener)
    }

    override fun getItemCount() = list.size
}
