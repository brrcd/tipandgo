package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tip_n_go.data.incoming.Review
import com.tip_n_go.databinding.FragmentReviewsBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.REVIEW_TAG
import com.tip_n_go.tools.listCastCheck
import com.tip_n_go.ui.adapter.ReviewsAdapter
import com.tip_n_go.ui.dialog.ReviewDialogFragment
import com.tip_n_go.ui.listener.ReviewListener
import com.tip_n_go.ui.listener.TopBarSwipeListener
import com.tip_n_go.viewmodel.ReviewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewsFragment :
    BaseFragment<FragmentReviewsBinding>(),
    ReviewListener,
    TopBarSwipeListener {

    private val viewModel: ReviewsViewModel by viewModel()
    private var reviewsList = listOf<Review>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.update()
        viewModel.organizationReviewsLiveData.observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun renderData(uiState: UiState<Any>) = with(binding) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<Review> { setupReviewsRecycler(it) }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupReviewsRecycler(reviews: List<Review>) {
        reviewsList = reviews
        val adapter = ReviewsAdapter()
        binding.reviewsRecycler.adapter = adapter
        adapter.updateList(reviews)
        adapter.setListener(this)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReviewsBinding {
        return FragmentReviewsBinding.inflate(inflater, container, false)
    }

    override fun onReviewSelected(position: Int) {
        val bottomDialog = ReviewDialogFragment()
        val bundle = Bundle().apply { putParcelable(REVIEW_TAG, reviewsList[position]) }
        bottomDialog.arguments = bundle
        bottomDialog.show(this.parentFragmentManager, "")
    }

    override fun onTopBarSwipe() {
        viewModel.update()
    }
}
