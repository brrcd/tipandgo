package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.databinding.FragmentTipsBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.listCastCheck
import com.tip_n_go.tools.showSnack
import com.tip_n_go.ui.adapter.TipsAdapter
import com.tip_n_go.ui.listener.TopBarSwipeListener
import com.tip_n_go.viewmodel.TipsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TipsFragment : BaseFragment<FragmentTipsBinding>(), TopBarSwipeListener {

    private val viewModel: TipsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.update()
        viewModel.organizationTipsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun renderData(uiState: UiState<Any>) = with(binding) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<Tip> { setupTipsRecycler(it) }
            }
            is UiState.Error -> {
                val data = uiState.exception
                if (data.error.isNotEmpty()) {
                    tipsTitle.showSnack(data.error)
                }
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupTipsRecycler(tips: List<Tip>) {
        val adapter = TipsAdapter()
        binding.tipsRecycler.adapter = adapter
        adapter.updateList(tips)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTipsBinding {
        return FragmentTipsBinding.inflate(inflater, container, false)
    }

    override fun onTopBarSwipe() {
        viewModel.update()
    }
}
