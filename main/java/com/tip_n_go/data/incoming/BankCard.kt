package com.tip_n_go.data.incoming

data class BankCard(
    val hash: String? = "",
    val cardType: String? = "",
    val expMonth: Int? = 0,
    val expYear: Int? = 0,
    val cardNumberMasked: String? = "",
    val cardHolderName: String? = ""
)
