package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tip_n_go.R
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.databinding.FragmentStaffListBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.ui.adapter.StaffAdapter
import com.tip_n_go.ui.listener.StaffListener
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.StaffListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffListFragment :
    BaseFragment<FragmentStaffListBinding>(),
    StaffListener,
    TopBarChangerInterface {

    private val viewModel: StaffListViewModel by viewModel()
    private var staffList = listOf<OrganizationUser>()
    private var organizationHash: String = ""
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }

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
        viewModel.getOrganizationUsers(organizationHash)
        binding.addNewUser.setOnClickListener {
            navigateTo(R.id.action_staffListFragment_to_addStaffFragment)
        }
        viewModel.organizationUsersLiveData.observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<OrganizationUser> { setupStaffRecycler(it) }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupStaffRecycler(staffList: List<OrganizationUser>) {
        this.staffList = staffList
        val adapter = StaffAdapter(this)
        binding.staffRecycler.adapter = adapter
        adapter.updateList(staffList)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStaffListBinding {
        return FragmentStaffListBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = getString(R.string.staff_list_title)
    }

    override fun restoreDefaultTopBar() {
        mainActivity.onReturnFromFragment()
    }

    override fun onStaffSelected(position: Int) {
        val bundle = Bundle().apply { putParcelable(ORGANIZATION_USER_TAG, staffList[position]) }
        navigateTo(R.id.action_staffListFragment_to_staffFragment, bundle)
    }
}
