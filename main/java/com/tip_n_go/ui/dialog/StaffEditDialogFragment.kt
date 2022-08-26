package com.tip_n_go.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Role
import com.tip_n_go.data.incoming.User
import com.tip_n_go.databinding.FragmentDialogStaffEditBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.ui.listener.DialogCloseButtonInterface
import com.tip_n_go.ui.listener.DialogDismissListener
import com.tip_n_go.viewmodel.StaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffEditDialogFragment(private val listener: DialogDismissListener) :
    BottomSheetDialogFragment(), DialogCloseButtonInterface {

    private var _binding: FragmentDialogStaffEditBinding? = null
    private val viewModel: StaffViewModel by viewModel()
    private val binding get() = _binding!!
    private var list = listOf<Role>()
    private var position = ""
    private var userHash = ""
    private var organizationHash = ""
    private var organizationUserRole: Role? = null

    override fun getTheme() = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list = arguments?.getParcelableArrayList(ROLES_LIST_TAG) ?: listOf()
        position = arguments?.getString(POSITION_TAG) ?: ""
        userHash = arguments?.getString(ORGANIZATION_USER_HASH) ?: ""
        organizationHash = arguments?.getString(ORGANIZATION_HASH) ?: ""
        organizationUserRole = arguments?.getParcelable(ORGANIZATION_USER_ROLE)
        _binding = FragmentDialogStaffEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupCloseButton()
        viewModel.userLiveData.observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                if (data is User) {
                    binding.root.showSnack(getString(R.string.employee_update_success))
                }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupView() {
        setupRolesSelector()
        binding.staffPosition.editText?.setText(position)
        setupSaveButton()
    }

    private fun setupRolesSelector() {
        val mList = list.map { it.name }.toMutableList()
        val adapter = createHintAdapter(mList)
        binding.roleSelector.adapter = adapter
        val selectedRole = (organizationUserRole?.id ?: 0) - 1
        binding.roleSelector.setSelection(selectedRole)
        setOnRoleSelected(list)
    }

    private fun setOnRoleSelected(roles: List<Role>) {
        binding.roleSelector.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, v: View?, index: Int, p3: Long) {
                if (index < roles.size) {
                    binding.staffRoleHint.gone()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val selectedRole = binding.roleSelector.selectedItem
            val selectedRoleId = list.first { it.name == selectedRole }.id ?: 0
            val position = binding.staffPosition.editText?.text.toString()
            viewModel.updateUser(userHash, organizationHash, position, selectedRoleId)
        }
    }

    override fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            this.dismiss()
            listener.onDismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener.onDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
