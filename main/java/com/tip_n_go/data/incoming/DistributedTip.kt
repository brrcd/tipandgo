package com.tip_n_go.data.incoming

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.DistributedTipDeserializer
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonAdapter(DistributedTipDeserializer::class)
data class DistributedTip(
    val date: String? = "",
    val user: UserBrief? = null,
    val amount: Double? = 0.0,
    val amountLocalized: String? = "",
    val currency: Currency? = null,
    val done: Boolean? = false,
    val source: List<Tip>? = null,
    val destinations: List<TipDestination>? = null
) : Parcelable
