package com.tip_n_go.data.incoming

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.TipDestinationDeserializer
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonAdapter(TipDestinationDeserializer::class)
data class TipDestination(
    val amount: Double? = 0.0,
    val amountLocalized: String? = "",
    val currency: Currency? = null,
    val user: UserBrief? = null
) : Parcelable
