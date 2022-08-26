package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Review
import com.tip_n_go.databinding.ItemReviewBinding
import com.tip_n_go.ui.listener.ReviewListener
import com.tip_n_go.ui.viewholder.ReviewsViewHolder

class ReviewsAdapter :
    RecyclerView.Adapter<ReviewsViewHolder>() {

    private var list = listOf<Review>()
    private var listener: ReviewListener? = null

    fun updateList(list: List<Review>) {
        this.list = list
        this.notifyItemRangeInserted(0, list.size)
    }

    fun setListener(listener: ReviewListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bindData(list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
