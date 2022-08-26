package com.tip_n_go.data.incoming

import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.BalanceDeserializer

@JsonAdapter(BalanceDeserializer::class)
data class Balance(
    val balanceLocalized: String? = "",
    val balance: Double? = 0.0,
    val currency: Currency? = null
)
