package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tip_n_go.R
import com.tip_n_go.data.incoming.DistributedTip
import com.tip_n_go.databinding.FragmentDistributedTipsBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.CURRENT_ORGANIZATION_HASH
import com.tip_n_go.tools.DISTRIBUTED_TIP_TAG
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.tools.listCastCheck
import com.tip_n_go.ui.adapter.DistributedTipsAdapter
import com.tip_n_go.ui.dialog.DistributedTipDialogFragment
import com.tip_n_go.ui.listener.TipDestinationListener
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.DistributedTipsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DistributedTipsFragment :
    BaseFragment<FragmentDistributedTipsBinding>(),
    TipDestinationListener,
    TopBarChangerInterface {

    private val viewModel: DistributedTipsViewModel by viewModel()
    private var distributedTipsList = listOf<DistributedTip>()
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    private var organizationHash = ""

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
        viewModel.distributedTipsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getDistributedTips(organizationHash)
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<DistributedTip> { setupDistributedTipsRecycler(it) }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupDistributedTipsRecycler(distributedTipsList: List<DistributedTip>) {
        this.distributedTipsList = distributedTipsList
        val adapter = DistributedTipsAdapter(this)
        binding.distributedTipsRecycler.adapter = adapter
        adapter.updateList(distributedTipsList)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDistributedTipsBinding {
        return FragmentDistributedTipsBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = getString(R.string.distributed_tips_fragment_title)
    }

    override fun restoreDefaultTopBar() {
        mainActivity.onReturnFromFragment()
    }

    override fun onTipDestinationSelected(position: Int) {
        val dialog = DistributedTipDialogFragment()
        val bundle =
            Bundle().apply { putParcelable(DISTRIBUTED_TIP_TAG, distributedTipsList[position]) }
        dialog.arguments = bundle
        dialog.show(this.parentFragmentManager, "")
    }
}
