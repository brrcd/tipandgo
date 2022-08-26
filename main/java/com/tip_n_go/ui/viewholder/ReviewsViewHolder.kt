package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Review
import com.tip_n_go.databinding.ItemReviewBinding
import com.tip_n_go.tools.setRatingWithDrawableEnd
import com.tip_n_go.ui.listener.ReviewListener

class ReviewsViewHolder(private val binding: ItemReviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(review: Review, listener: ReviewListener?) = with(binding) {
        val date = review.date
        val rating = review.rate ?: 0.0

        reviewDate.text = date
        reviewAverageRating.setRatingWithDrawableEnd(rating)

        root.setOnClickListener {
            listener?.onReviewSelected(adapterPosition)
        }
    }
}
