package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.databinding.ItemHeaderBinding

class TopBarAdapter(private val list: List<String>) : RecyclerView.Adapter<TopBarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopBarViewHolder {
        val binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopBarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopBarViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount() = list.size
}

class TopBarViewHolder(private val binding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(title: String) {
        binding.headerTitle.text = title
    }
}
