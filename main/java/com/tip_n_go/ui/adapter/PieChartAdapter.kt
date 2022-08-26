package com.tip_n_go.ui.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tip_n_go.data.incoming.ReportData
import com.tip_n_go.tools.REPORT_DATA_PAGE_TAG
import com.tip_n_go.tools.REPORT_DATA_TAG
import com.tip_n_go.ui.fragment.PieChartFragment

class PieChartAdapter(
    fragment: Fragment,
    private val reportData: ReportData
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = PieChartFragment()
        fragment.arguments = bundleOf(
            REPORT_DATA_TAG to reportData,
            REPORT_DATA_PAGE_TAG to position
        )
        return fragment
    }
}
