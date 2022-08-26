package com.tip_n_go.data.incoming

import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.PayoutDeserializer

@JsonAdapter(PayoutDeserializer::class)
data class Payout(
    val date: String? = "",
    val amount: Double? = 0.0,
    val amountLocalized: String? = "",
    val currency: Currency? = null,
    val userBankCard: BankCard? = null
)
