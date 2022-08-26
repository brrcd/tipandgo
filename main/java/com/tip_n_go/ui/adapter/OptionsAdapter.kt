package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Option
import com.tip_n_go.databinding.ItemOptionBinding
import com.tip_n_go.ui.listener.OptionListener
import com.tip_n_go.ui.viewholder.OptionsViewHolder

class OptionsAdapter : RecyclerView.Adapter<OptionsViewHolder>() {

    private var list = listOf<Option>()
    private var listener: OptionListener? = null

    fun updateList(list: List<Option>) {
        this.list = list
        notifyItemRangeInserted(0, list.size)
    }

    fun setListener(listener: OptionListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bindData(list[position], listener)
    }

    override fun getItemCount() = list.size
}
