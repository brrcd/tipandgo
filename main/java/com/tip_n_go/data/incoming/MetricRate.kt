package com.tip_n_go.data.incoming

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MetricRate(
    val name: String? = "",
    val rate: Int? = 0
) : Parcelable
