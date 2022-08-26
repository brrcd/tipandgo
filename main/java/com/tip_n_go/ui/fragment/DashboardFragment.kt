package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.highsoft.highcharts.common.hichartsclasses.*
import com.tip_n_go.R
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.incoming.*
import com.tip_n_go.databinding.FragmentDashboardBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.ui.adapter.PieChartAdapter
import com.tip_n_go.ui.listener.TopBarSwipeListener
import com.tip_n_go.viewmodel.DashboardViewModel
import com.tip_n_go.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(), TopBarSwipeListener {

    private val viewModel: DashboardViewModel by viewModel()
    private val activityViewModel: MainViewModel by viewModel()
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    private var organizationHash = ""
    private var organizationTipDistributionMethod = -1
    private var userOrganizationRoleId = -1
    private var currentUserHash = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        activityViewModel.organizationHashLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.organizationLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.organizationBalance.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.organizationUndistributedTips.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.tipReportsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.reviewReportsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getTipReportsData(PeriodType.WEEK)
        viewModel.getReviewReportsData(PeriodType.WEEK)
    }

    override fun renderData(uiState: UiState<Any>) = with(binding) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                if (data is Unit) {
                    organizationHash = sharedPrefs[CURRENT_ORGANIZATION_HASH] ?: ""
                    currentUserHash = sharedPrefs[CURRENT_USER_HASH] ?: ""
                    viewModel.getOrganizationByHash(organizationHash)
                }
                if (data is Organization) {
                    setupInfo(data)
                }
                if (data is Balance) {
                    setupBalance(data)
                }
                if (data is ReportData) {
                    if (data.type == "column") {
                        updateTipsChart(data)
                    } else if (data.type == "pie") {
                        updateReviewsChart(data)
                    }
                }
                data.listCastCheck<Tip> { setupBalance(it) }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupViews() {
        setupStaff()
        setupSettings()
        setupDistributedTips()
        setupTipsChips()
        setupReviewsChips()
    }

    private fun setupTipsChips() = with(binding.tipsChartCard) {
        day.setOnClickListener {
            viewModel.getTipReportsData(PeriodType.DAY)
        }
        week.setOnClickListener {
            viewModel.getTipReportsData(PeriodType.WEEK)
        }
        month.setOnClickListener {
            viewModel.getTipReportsData(PeriodType.MONTH)
        }
        year.setOnClickListener {
            viewModel.getTipReportsData(PeriodType.YEAR)
        }
    }

    private fun setupReviewsChips() = with(binding.reviewsChartCard) {
        day.setOnClickListener {
            viewModel.getReviewReportsData(PeriodType.DAY)
        }
        week.setOnClickListener {
            viewModel.getReviewReportsData(PeriodType.WEEK)
        }
        month.setOnClickListener {
            viewModel.getReviewReportsData(PeriodType.MONTH)
        }
        year.setOnClickListener {
            viewModel.getReviewReportsData(PeriodType.YEAR)
        }
    }

    private fun updateTipsChart(reportData: ReportData) {
        binding.tipsChartCard.tipsChart.visible()
        val chart = HIChart()
        val options = HIOptions()
        val title = HITitle()

        chart.type = reportData.type
        options.chart = chart
        title.text = reportData.title?.text
        options.title = title

        val yAxis = HIYAxis()
        yAxis.title = HITitle().apply {
            text = reportData.yAxis?.title?.text
        }
        options.yAxis = arrayListOf(yAxis)

        val xAxis = HIXAxis().apply {
            val cats = reportData.xAxis?.categories?.let { ArrayList(it) }
            categories = cats
        }
        options.xAxis = arrayListOf(xAxis)

        val exporting = HIExporting()
        options.exporting = exporting
        options.exporting.enabled = false

        val credits = HICredits()
        options.credits = credits
        options.credits.enabled = false

        val column = HIColumn()
        column.name = reportData.series.first().name
        val data = reportData.series.first().data?.let { ArrayList(it) }
        column.data = data
        options.series = arrayListOf(column)

        binding.tipsChartCard.tipsChart.options = options
        binding.tipsChartCard.tipsChart.update(options)
    }

    private fun updateReviewsChart(reportData: ReportData) = with(binding) {
        val adapter = PieChartAdapter(this@DashboardFragment, reportData)
        reviewsChartCard.reviewsViewPager.adapter = adapter
        TabLayoutMediator(reviewsChartCard.reviewsTabLayout, reviewsChartCard.reviewsViewPager) {
            _, _ ->
        }.attach()
    }

    private fun setupStaff() = with(binding) {
        staffCard.actionTitle.text = getString(R.string.staff_list_title)
        staffCard.actionDescription.text = getString(R.string.staff_description)
        val staffIcon = getDrawable(R.drawable.icon_staff)
        val staffIconColor = requireContext().getColor(R.color.blue)
        staffIcon?.setTint(staffIconColor)
        staffCard.actionIcon.setImageDrawable(staffIcon)
        staffCard.root.setOnClickListener {
            navigateTo(R.id.action_dashboardFragment_to_staffListFragment)
        }
    }

    private fun setupSettings() = with(binding) {
        settingsCard.actionTitle.text = getString(R.string.settings_and_notifications_title)
        settingsCard.actionDescription.text =
            getString(R.string.settings_and_notifications_description)
        val settingsIcon = getDrawable(R.drawable.icon_settings)
        val settingsIconColor = requireContext().getColor(R.color.blue)
        settingsIcon?.setTint(settingsIconColor)
        settingsCard.actionIcon.setImageDrawable(settingsIcon)
        settingsCard.root.setOnClickListener {
            val bundle = bundleOf(TIP_DISTRIBUTION_METHOD_TAG to organizationTipDistributionMethod)
            navigateTo(R.id.action_dashboardFragment_to_settingsFragment, bundle)
        }
    }

    private fun setupBalance(balance: Balance) = with(binding) {
        balanceCard.balanceTotal.text = balance.balanceLocalized
        balanceCard.actionButton.gone()
    }

    private fun setupBalance(list: List<Tip>) = with(binding) {
        var total = 0.0
        val currencySign = if (list.isNotEmpty()) {
            list.first().currency?.sign
        } else {
            "*"
        }
        list.map { tip -> tip.amount?.let { total += it } }
        val balanceWithCurrency = "$total $currencySign"
        balanceCard.balanceTotal.text = balanceWithCurrency
        if (total > 0.0) {
            balanceCard.actionButton.text = getString(R.string.distribute)
            balanceCard.actionButton.visible()
            balanceCard.actionButton.setOnClickListener {
                navigateTo(R.id.action_dashboardFragment_to_tipDistributionFragment)
            }
        } else {
            balanceCard.actionButton.gone()
        }
    }

    private fun setupDistributedTips() = with(binding) {
        distributedTipsCard.actionTitle.text = getString(R.string.distributed_tips_title)
        distributedTipsCard.actionDescription.text =
            getString(R.string.distributed_tips_description)
        val icon = getDrawable(R.drawable.icon_history)
        val iconColor = requireContext().getColor(R.color.blue)
        icon?.setTint(iconColor)
        distributedTipsCard.actionIcon.setImageDrawable(icon)
        distributedTipsCard.root.setOnClickListener {
            navigateTo(R.id.action_dashboardFragment_to_distributedTipsFragment)
        }
    }

    private fun setupInfo(data: User) = with(binding.infoTab) {
        title.text = data.firstName
        subtitle.text = data.lastName
        phoneNumber.text = data.phone
        emailAddress.text = data.email

        GlideApp.with(binding.root)
            .load(data.avatar)
            .into(avatar)
    }

    private fun setupInfo(data: Organization) = with(binding) {
        val title = data.name
        val subtitle = data.legalName
        val phoneNumber = data.phone
        val website = data.website
        val email = data.email
        val address = data.address
        val avatar = data.brand?.logo

        userOrganizationRoleId =
            data.organizationUsers?.first { it.hash == currentUserHash }?.organizationRole?.id ?: -1
        if (userOrganizationRoleId == 1) {
            settingsCard.root.visible()
        } else {
            settingsCard.root.gone()
        }

        organizationTipDistributionMethod = data.tipDistributionMethodId ?: -1
        if (organizationTipDistributionMethod == 1) {
            viewModel.getOrganizationBalance(organizationHash)
        } else if (organizationTipDistributionMethod == 2) {
            viewModel.getOrganizationUndistributedTips(organizationHash)
        }

        infoTab.title.text = title
        infoTab.subtitle.text = subtitle
        if (phoneNumber.isNullOrEmpty()) {
            infoTab.phoneHint.gone()
            infoTab.phoneNumber.gone()
        } else {
            infoTab.phoneHint.visible()
            infoTab.phoneNumber.visible()
            infoTab.phoneNumber.text = phoneNumber
        }
        if (website.isNullOrEmpty()) {
            infoTab.websiteHint.gone()
            infoTab.website.gone()
        } else {
            infoTab.websiteHint.visible()
            infoTab.website.visible()
            infoTab.website.text = website
        }
        if (address.isNullOrEmpty()) {
            infoTab.addressHint.gone()
            infoTab.address.gone()
        } else {
            infoTab.addressHint.visible()
            infoTab.address.visible()
            infoTab.address.text = address
        }
        if (email.isNullOrEmpty()) {
            infoTab.emailHint.gone()
            infoTab.emailAddress.gone()
        } else {
            infoTab.emailHint.visible()
            infoTab.emailAddress.visible()
            infoTab.emailAddress.text = email
        }

        GlideApp.with(binding.root)
            .load(avatar)
            .into(infoTab.avatar)

        infoTab.actionIconBackground.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ORGANIZATION_HASH, organizationHash)
            navigateTo(R.id.action_dashboardFragment_to_editOrganizationFragment2, bundle)
        }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    override fun onTopBarSwipe() {
        organizationHash = sharedPrefs[CURRENT_ORGANIZATION_HASH] ?: ""
        viewModel.getOrganizationByHash(organizationHash)
        viewModel.getTipReportsData(PeriodType.WEEK)
        viewModel.getReviewReportsData(PeriodType.WEEK)
    }
}
