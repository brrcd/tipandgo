package com.tip_n_go.data.incoming

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.TipDeserializer
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonAdapter(TipDeserializer::class)
data class Tip(
    val hash: String? = "",
    val date: String? = "",
    val tipRate: String? = "",
    val amount: Double? = 0.0,
    val amountLocalized: String? = "",
    val currency: Currency? = null,
    val user: UserBrief? = null,
    val organization: Organization? = null,
    val review: Review? = null
) : Parcelable
