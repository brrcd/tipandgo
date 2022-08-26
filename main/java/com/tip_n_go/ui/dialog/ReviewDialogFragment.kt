package com.tip_n_go.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tip_n_go.R
import com.tip_n_go.data.incoming.MetricRate
import com.tip_n_go.data.incoming.Review
import com.tip_n_go.databinding.FragmentDialogReviewBinding
import com.tip_n_go.tools.REVIEW_TAG
import com.tip_n_go.tools.gone
import com.tip_n_go.tools.setRatingWithDrawableEnd
import com.tip_n_go.tools.visible
import com.tip_n_go.ui.custom.CustomMetricRatingLayout
import com.tip_n_go.ui.listener.DialogCloseButtonInterface
import kotlin.properties.Delegates

class ReviewDialogFragment : BottomSheetDialogFragment(), DialogCloseButtonInterface {

    private var _binding: FragmentDialogReviewBinding? = null
    private val binding get() = _binding!!
    private var review by Delegates.notNull<Review>()

    override fun getTheme() = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        review = arguments?.getParcelable(REVIEW_TAG) ?: Review()
        _binding = FragmentDialogReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupCloseButton()
    }

    private fun setupView() {
        val tipAmount = review.tip?.amountLocalized
        val date = review.date
        val tipRate = review.tip?.tipRate
        val feedback = review.comments
        val averageRating = review.rate ?: 0.0

        binding.tipAmount.text = tipAmount
        binding.reviewDate.text = date
        binding.tipRate.text = tipRate
        review.metricRates?.forEachIndexed { index, it ->
            when (index) {
                0 -> {
                    showMetricRates(it, binding.firstMetricLayout)
                }
                1 -> {
                    showMetricRates(it, binding.secondMetricLayout)
                }
                2 -> {
                    showMetricRates(it, binding.thirdMetricLayout)
                }
            }
        }
        if (!feedback.isNullOrBlank()) {
            binding.feedbackTitle.visible()
            binding.feedbackContent.visible()
            binding.feedbackContent.text = feedback
        } else {
            binding.feedbackTitle.gone()
            binding.feedbackContent.gone()
        }
        binding.reviewAverageRating.setRatingWithDrawableEnd(averageRating)
    }

    override fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            this.dismiss()
        }
    }

    private fun showMetricRates(metricRate: MetricRate, metricBinding: CustomMetricRatingLayout) {
        val title = metricRate.name
        val rate = metricRate.rate ?: 0

        metricBinding.apply {
            setTitle(title)
            setRating(rate)
            visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
