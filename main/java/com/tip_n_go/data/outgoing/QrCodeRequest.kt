package com.tip_n_go.data.outgoing

class QrCodeRequest(
    val employeeHash: String?,
    val organizationHash: String?,
    val tableNo: Int?,
    val invoiceTotal: Int?
)
