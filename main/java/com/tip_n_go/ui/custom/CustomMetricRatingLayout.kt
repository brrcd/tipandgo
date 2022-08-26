package com.tip_n_go.ui.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.tip_n_go.R
import com.tip_n_go.databinding.CustomMetricRatingLayoutBinding
import com.tip_n_go.tools.getDrawableResource

class CustomMetricRatingLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private var _binding: CustomMetricRatingLayoutBinding? = null
    val binding get() = _binding!!

    init {
        _binding = CustomMetricRatingLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setTitle(text: String?) {
        binding.metricTitle.text = text
    }

    fun setRating(rating: Int) = with(binding) {
        val emptyStarDrawable = context.getDrawableResource(R.drawable.empty_star)
        val emptyStarColor = context.getColor(R.color.grey)
        emptyStarDrawable?.setTint(emptyStarColor)
        when (rating) {
            1 -> {
                setDrawable(secondStar, emptyStarDrawable)
                setDrawable(thirdStar, emptyStarDrawable)
                setDrawable(fourthStar, emptyStarDrawable)
                setDrawable(fifthStar, emptyStarDrawable)
            }
            2 -> {
                setDrawable(thirdStar, emptyStarDrawable)
                setDrawable(fourthStar, emptyStarDrawable)
                setDrawable(fifthStar, emptyStarDrawable)
            }
            3 -> {
                setDrawable(fourthStar, emptyStarDrawable)
                setDrawable(fifthStar, emptyStarDrawable)
            }
            4 -> {
                setDrawable(fifthStar, emptyStarDrawable)
            }
            else -> {
                return@with
            }
        }
    }

    private fun setDrawable(imageView: ImageView, drawable: Drawable?) {
        imageView.apply {
            setImageDrawable(drawable)
            background = null
        }
    }
}
