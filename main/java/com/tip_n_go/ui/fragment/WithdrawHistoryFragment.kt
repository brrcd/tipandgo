package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Payout
import com.tip_n_go.databinding.FragmentWithdrawHistoryBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.gone
import com.tip_n_go.tools.listCastCheck
import com.tip_n_go.ui.adapter.PayoutsAdapter
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.WithdrawHistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WithdrawHistoryFragment :
    BaseFragment<FragmentWithdrawHistoryBinding>(),
    TopBarChangerInterface {

    private val viewModel: WithdrawHistoryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopBarTitle()
        viewModel.payoutHistoryLiveData.observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<Payout> { setupPayoutsRecycler(it) }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupPayoutsRecycler(payouts: List<Payout>) {
        val adapter = PayoutsAdapter()
        binding.payoutsRecycler.adapter = adapter
        adapter.updateList(payouts)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWithdrawHistoryBinding {
        return FragmentWithdrawHistoryBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = getString(R.string.history_of_withdrawals_title)
        mainActivity.binding.openNotifications.gone()
    }

    override fun restoreDefaultTopBar() {}
}
