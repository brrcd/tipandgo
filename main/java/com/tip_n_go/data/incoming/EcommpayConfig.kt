package com.tip_n_go.data.incoming

class EcommpayConfig(
    val project_id: Int?,
    val payment_id: String?,
    val payment_amount: Long?,
    val payment_currency: String?,
    val payment_description: String?,
    val customer_id: String?,
    val region_code: String?,
    val signature: String?,
    val hide_saved_wallets: Int?,
    val language_code: String?
)
