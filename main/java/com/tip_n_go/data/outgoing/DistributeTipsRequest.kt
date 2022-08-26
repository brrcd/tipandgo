package com.tip_n_go.data.outgoing

class DistributeTipsRequest(
    val organizationHash: String?,
    val tipHashes: List<String>?,
    val userHashes: List<String?>,
    val amount: Int?
)
