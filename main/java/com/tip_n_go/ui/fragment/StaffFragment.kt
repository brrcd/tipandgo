package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.highsoft.highcharts.common.hichartsclasses.*
import com.tip_n_go.R
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.incoming.*
import com.tip_n_go.databinding.FragmentStaffBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.ui.adapter.PieChartAdapter
import com.tip_n_go.ui.adapter.ReviewsAdapter
import com.tip_n_go.ui.adapter.TipsAdapter
import com.tip_n_go.ui.dialog.ReviewDialogFragment
import com.tip_n_go.ui.dialog.StaffEditDialogFragment
import com.tip_n_go.ui.listener.DialogDismissListener
import com.tip_n_go.ui.listener.ReviewListener
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.StaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class StaffFragment :
    BaseFragment<FragmentStaffBinding>(),
    TopBarChangerInterface,
    DialogDismissListener,
    ReviewListener{

    private val viewModel: StaffViewModel by viewModel()
    private var organizationUser by Delegates.notNull<OrganizationUser>()
    private var availableRoles: List<Role> = listOf()
    private var organizationUserRole by Delegates.notNull<Role>()
    private var organizationUserPosition = ""
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    private var organizationHash = ""
    private var organizationUserHash = ""
    private var reviewsList = listOf<Review>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        organizationUser = arguments?.getParcelable(ORGANIZATION_USER_TAG) ?: OrganizationUser()
        organizationHash = sharedPrefs[CURRENT_ORGANIZATION_HASH] ?: ""
        organizationUserHash = organizationUser.hash ?: ""
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.rolesLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.tipsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.userLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.tipReportsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.reviewReportsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getTipReportsData(PeriodType.WEEK)
        viewModel.getReviewReportsData(PeriodType.WEEK)
        viewModel.getAvailableRoles(organizationHash)
        viewModel.getUserInOrganization(organizationUserHash, organizationHash)
        viewModel.getTips(organizationUserHash, organizationHash)
        viewModel.getReviews(organizationUserHash, organizationHash)
        setupViews()
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<Review> { setupReviewsRecycler(it) }
                data.listCastCheck<Tip> { setupTipsRecycler(it) }
                data.listCastCheck<Role> { updateRolesList(it) }
                if (data is User) {
                    setupInfoTab(data)
                }
                if (data is ReportData) {
                    if (data.type == "column") {
                        updateTipsChart(data)
                    } else if (data.type == "pie") {
                        updateReviewsChart(data)
                    }
                }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupTipsRecycler(listOfTips: List<Tip>) {
        val adapter = TipsAdapter()
        binding.tipsRecycler.adapter = adapter
        adapter.updateList(listOfTips)
    }

    private fun setupReviewsRecycler(listOfReviews: List<Review>) {
        this.reviewsList = listOfReviews
        val adapter = ReviewsAdapter()
        adapter.setListener(this)
        binding.reviewsRecycler.adapter = adapter
        adapter.updateList(listOfReviews)
    }

    private fun updateRolesList(list: List<Role>) {
        if (list.isNotEmpty()) {
            availableRoles = list
        }
    }

    private fun setupViews() {
        setupTopBarTitle()
        setupTabLayout()
        setupOpenQrCode()
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
        val adapter = PieChartAdapter(this@StaffFragment, reportData)
        reviewsChartCard.reviewsViewPager.adapter = adapter
        TabLayoutMediator(reviewsChartCard.reviewsTabLayout, reviewsChartCard.reviewsViewPager) {
            _, _ ->
        }.attach()
    }

    private fun setupInfoTab(user: User) = with(binding) {
        user.userOrganizations?.first().let { organizationBrief ->
            organizationBrief?.position?.let { organizationUserPosition = it }
            organizationBrief?.organizationRole.let {
                it?.let { organizationUserRole = it }
            }
        }
        val title = user.fullName
        val subtitle = user.userOrganizations?.first()?.position
        val avatar = user.avatar
        val phone = user.phone
        val email = user.email
        val rating = user.rating ?: 0.0

        infoTab.title.text = title
        infoTab.subtitle.text = subtitle
        if (phone.isNullOrEmpty()) {
            infoTab.phoneNumber.gone()
            infoTab.phoneHint.gone()
        } else {
            infoTab.phoneNumber.text = phone
        }
        if (email.isNullOrEmpty()) {
            infoTab.emailAddress.gone()
            infoTab.emailHint.gone()
        } else {
            infoTab.emailAddress.text = email
        }
        infoTab.rating.setRatingWithDrawableEnd(rating)

        GlideApp.with(root.context)
            .load(avatar)
            .into(infoTab.avatar)
    }

    private fun setupTabLayout() {
        fillWithTabs()
        setTabListener()
    }

    private fun setupOpenQrCode() = with(binding) {
        qrCodeTab.actionTitle.text = getString(R.string.employee_qr_code)
        qrCodeTab.actionDescription.text = getString(R.string.employee_qr_code_description)
        val qrIcon = getDrawable(R.drawable.icon_qr_code)
        val qrIconColor = requireContext().getColor(R.color.blue)
        qrIcon?.setTint(qrIconColor)
        qrCodeTab.actionIcon.setImageDrawable(qrIcon)
        qrCodeTab.root.setOnClickListener {
            val bundle = bundleOf(
                ORGANIZATION_USER_HASH to organizationUserHash,
                ORGANIZATION_HASH to organizationHash
            )
            navigateTo(R.id.action_staffFragment_to_qrCodeFragment2, bundle)
        }
    }

    private fun fillWithTabs() = with(binding.tabLayout) {
        root.newTab().apply {
            text = getString(R.string.info_tab)
            root.addTab(this)
        }
        root.newTab().apply {
            text = getString(R.string.tips_tab)
            root.addTab(this)
        }
        root.newTab().apply {
            text = getString(R.string.reviews_tab)
            root.addTab(this)
        }
    }

    private fun setTabListener() {
        binding.tabLayout.root.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.infoLayout.visible()
                        binding.tipsRecycler.gone()
                        binding.reviewsRecycler.gone()
                    }
                    1 -> {
                        binding.infoLayout.gone()
                        binding.tipsRecycler.visible()
                        binding.reviewsRecycler.gone()
                    }
                    2 -> {
                        binding.infoLayout.gone()
                        binding.tipsRecycler.gone()
                        binding.reviewsRecycler.visible()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showEditDialog() {
        val bottomDialog = StaffEditDialogFragment(this)
        val array = arrayListOf<Role>().apply { addAll(availableRoles) }
        val bundle = Bundle().apply {
            putParcelableArrayList(ROLES_LIST_TAG, array)
            putString(POSITION_TAG, organizationUserPosition)
            putString(ORGANIZATION_USER_HASH, organizationUserHash)
            putString(ORGANIZATION_HASH, organizationHash)
            putParcelable(ORGANIZATION_USER_ROLE, organizationUserRole)
        }
        bottomDialog.arguments = bundle
        bottomDialog.show(this@StaffFragment.parentFragmentManager, "")
    }

    override fun onDismiss() {
        viewModel.getUserInOrganization(organizationUserHash, organizationHash)
    }

    override fun onReviewSelected(position: Int) {
        val bottomDialog = ReviewDialogFragment()
        val bundle = Bundle().apply { putParcelable(REVIEW_TAG, reviewsList[position]) }
        bottomDialog.arguments = bundle
        bottomDialog.show(this.parentFragmentManager, "")
    }

    override fun setupTopBarTitle() {
        val userName = organizationUser.fullName
        mainActivity.binding.openSettings.apply {
            visible()
            setOnClickListener {
                showEditDialog()
            }
        }
        if (userName.isBlank()) {
            mainActivity.binding.topBarTitle.text = getString(R.string.employee_name)
        } else {
            mainActivity.binding.topBarTitle.text = userName
        }
    }

    override fun restoreDefaultTopBar() {
        mainActivity.binding.openSettings.gone()
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStaffBinding {
        return FragmentStaffBinding.inflate(inflater, container, false)
    }
}
