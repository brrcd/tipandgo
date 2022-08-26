package com.tip_n_go.data.incoming

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.TipShortDeserializer
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonAdapter(TipShortDeserializer::class)
data class TipBrief(
    val hash: String? = "",
    val date: String? = "",
    val tipRate: String? = "",
    val amount: Double? = 0.0,
    val amountLocalized: String? = "",
    val currency: Currency? = null
) : Parcelable
