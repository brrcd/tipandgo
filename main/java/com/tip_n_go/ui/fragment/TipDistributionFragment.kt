package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tip_n_go.R
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.databinding.FragmentTipDistributionBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.ui.activity.MainActivity
import com.tip_n_go.ui.adapter.TipDistributionEmployeeAdapter
import com.tip_n_go.ui.adapter.TipDistributionTipsAdapter
import com.tip_n_go.ui.listener.CheckBoxListener
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.TipDistributionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TipDistributionFragment :
    BaseFragment<FragmentTipDistributionBinding>(),
    TopBarChangerInterface,
    CheckBoxListener {

    private val viewModel: TipDistributionViewModel by viewModel()
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    private var organizationHash = ""
    private var employeeList = listOf<OrganizationUser>()
    private var tipsList = listOf<Tip>()
    private val employeeAdapter by lazy { TipDistributionEmployeeAdapter() }
    private val tipsAdapter by lazy { TipDistributionTipsAdapter() }
    private var selectedEmployeeList = mutableSetOf<OrganizationUser>()
    private var selectedTipList = mutableSetOf<Tip>()
    private var currentSelectionId = TIPS
    private var isManuallyChecked = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        organizationHash = sharedPrefs[CURRENT_ORGANIZATION_HASH] ?: ""
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.getInfo(organizationHash)
        viewModel.organizationUndistributedTips.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.organizationUsersLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.distributedTipsLiveData.observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<OrganizationUser> { setupEmployeeRecycler(it) }
                data.listCastCheck<Tip> { setupTipsRecycler(it) }
                if (data is Unit) {
                    showSuccess()
                }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupView() {
        selectEmployeeButton()
        setupBackButton()
        setupDistributeButton()
        setupSelectAllCheckbox()
    }

    private fun showSuccess() {
        binding.successLayout.root.visible()
        binding.successLayout.successDescription.text =
            getString(R.string.tips_distributed_successfully)
        binding.distributionLayout.gone()
        binding.distributionButtonsLayout.gone()
        binding.selectEmployeeButton.text = getString(R.string.ok_text)
        binding.selectEmployeeButton.visible()
        binding.selectEmployeeButton.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun selectEmployeeButton() {
        binding.selectEmployeeButton.setOnClickListener {
            binding.employeeRecycler.visible()
            binding.tipRecycler.gone()
            binding.title.text = getString(R.string.select_employee_title)
            binding.backButton.visible()
            binding.distributeButton.visible()
            it.gone()
            currentSelectionId = EMPLOYEE
            checkCheckBox()
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            binding.employeeRecycler.gone()
            binding.tipRecycler.visible()
            binding.title.text = getString(R.string.select_tips_title)
            binding.backButton.gone()
            binding.distributeButton.gone()
            binding.selectEmployeeButton.visible()
            it.gone()
            currentSelectionId = TIPS
            checkCheckBox()
        }
    }

    private fun checkCheckBox() {
        when (currentSelectionId) {
            EMPLOYEE -> {
                autoCheckSelectAllCheckbox(selectedEmployeeList.size == employeeList.size && employeeList.isNotEmpty())
            }
            TIPS -> {
                autoCheckSelectAllCheckbox(selectedTipList.size == tipsList.size && tipsList.isNotEmpty())
            }
        }
    }

    private fun setupDistributeButton() {
        binding.distributeButton.setOnClickListener {
            viewModel.distributeTips(
                organizationHash,
                selectedTipList.toList(),
                selectedEmployeeList.toList()
            )
        }
    }

    // todo error when unchecking
    // view holders onCheckedChangeListener doesn't trigger
    private fun setupSelectAllCheckbox() {
        binding.selectAllCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isManuallyChecked) {
                when (currentSelectionId) {
                    EMPLOYEE -> {
                        employeeAdapter.checkAll(isChecked)
                    }
                    TIPS -> {
                        tipsAdapter.checkAll(isChecked)
                    }
                }
            }
        }
    }

    private fun setupEmployeeRecycler(list: List<OrganizationUser>) {
        this.employeeList = list
        binding.employeeRecycler.adapter = employeeAdapter
        employeeAdapter.updateList(list)
        employeeAdapter.setListener(this)
    }

    private fun setupTipsRecycler(list: List<Tip>) {
        this.tipsList = list
        binding.tipRecycler.adapter = tipsAdapter
        binding.tipRecycler.visible()
        tipsAdapter.updateList(list)
        tipsAdapter.setListener(this)
    }

    override fun onCheck(position: Int, isChecked: Boolean) {
        if (isChecked) {
            when (currentSelectionId) {
                EMPLOYEE -> {
                    selectedEmployeeList.add(employeeList[position])
                    autoCheckSelectAllCheckbox(selectedEmployeeList.size == employeeList.size)
                }
                TIPS -> {
                    selectedTipList.add(tipsList[position])
                    autoCheckSelectAllCheckbox(selectedTipList.size == tipsList.size)
                }
            }
        } else {
            when (currentSelectionId) {
                EMPLOYEE -> {
                    selectedEmployeeList.remove(employeeList[position])
                }
                TIPS -> {
                    selectedTipList.remove(tipsList[position])
                }
            }
            autoCheckSelectAllCheckbox(isChecked)
        }
        updateTipSum()
    }

    private fun autoCheckSelectAllCheckbox(isChecked: Boolean) {
        isManuallyChecked = false
        binding.selectAllCheckbox.isChecked = isChecked
        isManuallyChecked = true
    }

    // todo implement this one
    private fun updateTipSum() {
        var totalSum = 0.0
        selectedTipList.map { tip -> tip.amount?.let { totalSum += it } }
        val defaultText = getString(R.string.distribute)
        val updatedText = if (totalSum != 0.0) {
            "$defaultText + $totalSum"
        } else {
            defaultText
        }
        binding.selectEmployeeButton.text = updatedText
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTipDistributionBinding {
        return FragmentTipDistributionBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        val activityBinding = (requireActivity() as MainActivity).binding
        activityBinding.topBarTitle.text = getString(R.string.tip_distribution_title)
    }

    override fun restoreDefaultTopBar() {
        (activity as MainActivity).onReturnFromFragment()
    }

    companion object {
        private const val EMPLOYEE = 0
        private const val TIPS = 1
    }
}
