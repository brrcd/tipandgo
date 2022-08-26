package com.tip_n_go.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.highsoft.highcharts.common.hichartsclasses.*
import com.tip_n_go.App
import com.tip_n_go.R
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.incoming.*
import com.tip_n_go.databinding.FragmentProfileBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.ui.activity.LoginActivity
import com.tip_n_go.ui.adapter.PieChartAdapter
import com.tip_n_go.ui.dialog.PayoutUserDialogFragment
import com.tip_n_go.ui.listener.DialogDismissListener
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.MainViewModel
import com.tip_n_go.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(),
    TopBarChangerInterface,
    DialogDismissListener {

    private val viewModel: ProfileViewModel by viewModel()
    private val activityViewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        viewModel.balanceLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.tipReportsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.reviewReportsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.removeFcmTokenLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.notificationLiveData.observe(viewLifecycleOwner) { renderData(it) }
        activityViewModel.userLiveData.observe(viewLifecycleOwner) { renderData(it) }
        activityViewModel.getUser()
        viewModel.getBalance()
        viewModel.getTipReportsData(PeriodType.WEEK)
        viewModel.getReviewReportsData(PeriodType.WEEK)
        viewModel.getUserNotifications()
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                if (data is Balance) {
                    setupBalance(data)
                }
                if (data is User) {
                    setUserName(data)
                }
                if (data is UnitResponseResult) {
                    if (data.removeFcmTokenSuccess) {
                        openLogin()
                    }
                }
                if (data is ReportData) {
                    if (data.type == "column") {
                        updateTipsChart(data)
                    } else if (data.type == "pie") {
                        updateReviewsChart(data)
                    }
                }
                data.listCastCheck<Notification> { showNotificationBadge(it) }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupViews() {
        setupWithdrawHistory()
        setupOrganizations()
        setupSettings()
        setupSignOut()
        setupTipsChips()
        setupReviewsChips()
    }

    private fun setupBalance(balance: Balance) {
        binding.balanceCard.balanceTotal.text = balance.balanceLocalized
        binding.balanceCard.actionButton.setOnClickListener {
            val bottomDialog = PayoutUserDialogFragment(this)
            bottomDialog.show(this.parentFragmentManager, "")
        }
    }

    private fun setupWithdrawHistory() = with(binding) {
        historyCard.actionTitle.text = getString(R.string.history_of_withdrawals_title)
        historyCard.actionDescription.text =
            getString(R.string.history_of_withdrawals_description)
        val historyIcon = getDrawable(R.drawable.icon_history)
        val historyIconColor = requireContext().getColor(R.color.blue)
        historyIcon?.setTint(historyIconColor)
        historyCard.actionIcon.setImageDrawable(historyIcon)
        historyCard.root.setOnClickListener {
            navigateTo(R.id.action_profileFragment_to_withdrawHistoryFragment)
        }
    }

    private fun setupOrganizations() = with(binding) {
        organizationsCard.actionTitle.text = getString(R.string.organizations_title)
        organizationsCard.actionDescription.text = getString(R.string.organizations_description)
        val organizationsIcon = getDrawable(R.drawable.icon_dashboard)
        val organizationsIconColor = requireContext().getColor(R.color.blue)
        organizationsIcon?.setTint(organizationsIconColor)
        organizationsCard.actionIcon.setImageDrawable(organizationsIcon)
        organizationsCard.root.setOnClickListener {
            navigateTo(R.id.action_profileFragment_to_organizationsFragment)
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
            navigateTo(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    private fun showNotificationBadge(notifications: List<Notification>){
        val hasUnread = notifications.any { !it.isRead }
        if (hasUnread) {
            val badge = BadgeDrawable.create(requireContext())
            badge.backgroundColor = requireContext().getColor(R.color.red)
            BadgeUtils.attachBadgeDrawable(badge, mainActivity.binding.openNotifications)
            badge.isVisible = true
        }
    }

    private fun setupSignOut() = with(binding) {
        signOutCard.actionTitle.text = getString(R.string.sign_out)
        val signOutTitleColor = requireContext().getColor(R.color.red)
        signOutCard.actionTitle.setTextColor(signOutTitleColor)
        signOutCard.actionDescription.gone()
        val signOutIcon = getDrawable(R.drawable.icon_sign_out)
        signOutCard.actionIcon.setImageDrawable(signOutIcon)
        val signOutIconBackground = getDrawable(R.drawable.card_button_background_lightred)
        signOutCard.actionIconBackground.background = signOutIconBackground
        signOutCard.root.setOnClickListener {
            signOut()
        }
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

    private fun signOut() {
        SharedTools.removeJwtToken()
        viewModel.removeFcmToken()
    }

    private fun openLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        App.application.startActivity(intent)
        requireActivity().finish()
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
        val adapter = PieChartAdapter(this@ProfileFragment, reportData)
        reviewsChartCard.reviewsViewPager.adapter = adapter
        TabLayoutMediator(reviewsChartCard.reviewsTabLayout, reviewsChartCard.reviewsViewPager) {
            _, _ ->
        }.attach()
    }

    override fun setupTopBarTitle() {
        // todo fix name
        val activityBinding = mainActivity.binding
        activityBinding.arrowBack.gone()
        activityBinding.viewPager.gone()
        activityBinding.openQrCode.gone()
        activityBinding.tabLayout.gone()
        activityBinding.openNotifications.apply {
            visible()
            setOnClickListener {
                navigateTo(R.id.action_profileFragment_to_notificationsFragment)
            }
        }
    }

    private fun setUserName(user: User) {
        mainActivity.binding.topBarTitle.apply {
            text = user.fullName
            visible()
        }
    }

    override fun restoreDefaultTopBar() {
        mainActivity.onReturnFromFragment()
    }

    override fun onDismiss() {
        viewModel.getBalance()
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }
}
