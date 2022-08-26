package com.tip_n_go.data.outgoing

import com.tip_n_go.data.ChartType
import com.tip_n_go.data.PeriodType

class ReportDataRequest(
    val charts: List<Chart>? = null
)

class Chart(
    val userHash: String? = null,
    val organizationHash: String? = null,
    private val periodEnum: PeriodType? = null,
    private val chartTypeEnum: ChartType? = null
) {
    val period: Int? = periodEnum?.ordinal
    val chartType: Int? = chartTypeEnum?.ordinal
}
