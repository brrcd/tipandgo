package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Settings
import com.tip_n_go.data.incoming.UnitResponseResult
import com.tip_n_go.data.incoming.UpdateOrganizationSettings
import com.tip_n_go.data.incoming.UpdateSettings
import com.tip_n_go.databinding.FragmentSettingsBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.ui.adapter.OptionsAdapter
import com.tip_n_go.ui.listener.OptionListener
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment :
    BaseFragment<FragmentSettingsBinding>(),
    TopBarChangerInterface,
    OptionListener {

    private val viewModel: SettingsViewModel by viewModel()
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    private var organizationHash = ""
    private var tipDistributionMethod = -1
    private val adapter by lazy { OptionsAdapter() }
    private val checkBoxes = mutableSetOf<CheckBox>()

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
        viewModel.getOrganizationSettings(organizationHash)
        viewModel.tipsDistributionMethodsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.settingsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.settingsUpdateSuccess.observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<Settings> { setupOptionsRecycler(it) }
                if (data is UnitResponseResult) {
                    if (data.settingsUpdateSuccess) {
                        binding.saveButton.showSnack(getString(R.string.settings_saved))
                    }
                }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupView() {
        binding.saveButton.setOnClickListener {
            val settings = UpdateOrganizationSettings(
                listOf(
                    UpdateSettings(
                        TIPS_DISTRIBUTION_METHOD,
                        listOf(tipDistributionMethod)
                    )
                )
            )
            viewModel.updateOrganizationSettings(organizationHash, settings)
            checkBoxes.clear()
        }
    }

    private fun setupOptionsRecycler(list: List<Settings>) {
        val tipSettings = list.first { it.key == TIPS_DISTRIBUTION_METHOD }
        val title = tipSettings.name
        val subtitle = tipSettings.description
        val optionsList = tipSettings.options ?: listOf()

        binding.optionsRecycler.adapter = adapter
        adapter.updateList(optionsList)
        adapter.setListener(this)
        binding.title.text = title
        binding.subtitle.text = subtitle
    }

    override fun onOptionSelected(position: Int, optionId: Int, isChecked: Boolean) {
        checkBoxes.forEachIndexed { index, checkBox ->
            if (position != index) {
                checkBox.isChecked = !isChecked
            }
        }
        tipDistributionMethod = optionId
    }

    override fun onOptionViewCreated(view: CheckBox) {
        checkBoxes.add(view)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = getString(R.string.settings_title)
    }

    override fun restoreDefaultTopBar() {}
}
