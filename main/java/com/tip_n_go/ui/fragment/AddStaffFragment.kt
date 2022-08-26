package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Role
import com.tip_n_go.databinding.FragmentAddStaffBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.ui.activity.MainActivity
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.EditStaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddStaffFragment : BaseFragment<FragmentAddStaffBinding>(), TopBarChangerInterface {

    private val viewModel: EditStaffViewModel by viewModel()
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    val organizationHash = sharedPrefs[CURRENT_ORGANIZATION_HASH] ?: ""
    private var roles = listOf<Role>()
    private var selectedRole = Role()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.rolesLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.userLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getAvailableRoles(organizationHash)
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<Role> {
                    roles = it
                    setupRolesSelector(it)
                }
                if (data is Unit) {
                    showSuccess()
                }
            }
            is UiState.Error -> {
                uiState.showError(binding.saveButton)
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupView() {
        binding.saveButton.setOnClickListener {
            val lastName = binding.staffLastName.getText
            val firstName = binding.staffFirstName.getText
            val email = binding.staffEmail.getText
            val position = binding.staffPosition.getText
            val phone = filterPhoneNumber()
            val roleId = selectedRole.id ?: 3
            viewModel.registerUserToOrganization(
                phone, email, lastName, firstName, organizationHash, position, roleId
            )
        }
    }

    private fun showSuccess() {
        binding.successLayout.root.visible()
        binding.successLayout.successDescription.text = getString(R.string.user_successfully_added)
        binding.inputLayout.gone()
        binding.saveButton.text = getString(R.string.ok_text)
        binding.saveButton.visible()
        binding.saveButton.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun setupRolesSelector(list: List<Role>) {
        // todo role selection
        val mList = list.map { it.name }.toMutableList()
        val adapter = createHintAdapter(mList)
        binding.roleSelector.adapter = adapter
        binding.roleSelector.setSelection(adapter.count)
        setOnRoleSelected(list)
    }

    private fun setOnRoleSelected(roles: List<Role>) {
        binding.roleSelector.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, v: View?, index: Int, p3: Long) {
                if (index < roles.size) {
                    binding.roleHint.gone()
                    selectedRole = roles[index]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun filterPhoneNumber(): String {
        return binding.staffPhone.editText.text.toString().filter { it.isDigit() }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddStaffBinding {
        return FragmentAddStaffBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        val activityBinding = (requireActivity() as MainActivity).binding
        activityBinding.topBarTitle.text = getString(R.string.add_an_employee)
    }

    override fun restoreDefaultTopBar() {}
}
