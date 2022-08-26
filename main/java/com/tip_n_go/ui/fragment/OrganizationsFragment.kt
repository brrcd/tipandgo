package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Organization
import com.tip_n_go.databinding.FragmentOrganizationsBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.ORGANIZATION_HASH
import com.tip_n_go.tools.gone
import com.tip_n_go.tools.listCastCheck
import com.tip_n_go.ui.adapter.OrganizationsAdapter
import com.tip_n_go.ui.listener.OrganizationListener
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.OrganizationsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrganizationsFragment :
    BaseFragment<FragmentOrganizationsBinding>(),
    TopBarChangerInterface,
    OrganizationListener {

    private val viewModel: OrganizationsViewModel by viewModel()
    private var organizationList = listOf<Organization>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewOrganization.setOnClickListener {
            navigateTo(R.id.action_organizationsFragment_to_editOrganizationFragment)
        }
        viewModel.organizationLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getUserOrganizations()
    }

    override fun renderData(uiState: UiState<Any>) = with(binding) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<Organization> { setupOrganizationsRecycler(it) }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupOrganizationsRecycler(organizations: List<Organization>) {
        organizationList = organizations
        val adapter = OrganizationsAdapter()
        binding.organizationsRecycler.adapter = adapter
        adapter.updateList(organizations)
        adapter.setListener(this)
    }

    override fun onOrganizationSelected(position: Int) {
        val organizationHash = organizationList[position].hash ?: ""
        val bundle = Bundle()
        bundle.putString(ORGANIZATION_HASH, organizationHash)
        navigateTo(R.id.action_organizationsFragment_to_editOrganizationFragment, bundle)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrganizationsBinding {
        return FragmentOrganizationsBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = getString(R.string.organizations_title)
        mainActivity.binding.openNotifications.gone()
    }

    override fun restoreDefaultTopBar() {}
}
