package com.tip_n_go.data.incoming

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.ReviewDeserializer
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonAdapter(ReviewDeserializer::class)
data class Review(
    val date: String? = "",
    val comments: String? = "",
    val rate: Double? = null,
    val metricRates: List<MetricRate>? = null,
    val tip: TipBrief? = null
) : Parcelable
