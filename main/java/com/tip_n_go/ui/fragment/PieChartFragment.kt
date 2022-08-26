package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.highsoft.highcharts.common.hichartsclasses.*
import com.tip_n_go.data.incoming.ReportData
import com.tip_n_go.databinding.FragmentPieChartBinding
import com.tip_n_go.tools.REPORT_DATA_PAGE_TAG
import com.tip_n_go.tools.REPORT_DATA_TAG
import com.tip_n_go.tools.visible
import kotlin.properties.Delegates

class PieChartFragment : Fragment() {

    private var _binding: FragmentPieChartBinding? = null
    private val binding get() = _binding!!
    private var reportData: ReportData by Delegates.notNull()
    private var page = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPieChartBinding.inflate(inflater, container, false)
        reportData = arguments?.getParcelable(REPORT_DATA_TAG) ?: ReportData()
        page = arguments?.getInt(REPORT_DATA_PAGE_TAG) ?: -1
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPieChart()
    }

    private fun setupPieChart() {
        binding.reviewsChart.visible()
        val chart = HIChart()
        val options = HIOptions()
        val title = HITitle()
        val subtitle = HISubtitle()
        val exporting = HIExporting()
        val credits = HICredits()
        val pie = HIPie()
        val data = reportData.series[page].dataPie?.let { ArrayList(it) }

        chart.type = reportData.type
        options.chart = chart

        title.text = reportData.title?.text
        options.title = title

        subtitle.text = reportData.series[page].name
        subtitle.style = HICSSObject()
        subtitle.style.fontSize = "16px"
        options.subtitle = subtitle

        exporting.enabled = false
        options.exporting = exporting

        credits.enabled = false
        options.credits = credits

        pie.data = data
        options.series = arrayListOf(pie)
        binding.reviewsChart.options = options
        binding.reviewsChart.update(options)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
