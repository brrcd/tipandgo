package com.tip_n_go.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tip_n_go.databinding.FragmentPasswordRecoveryBinding
import com.tip_n_go.state.UiState

class PasswordRecoveryFragment : BaseFragment<FragmentPasswordRecoveryBinding>() {

    override fun renderData(uiState: UiState<Any>) {
        TODO("Not yet implemented")
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPasswordRecoveryBinding {
        return FragmentPasswordRecoveryBinding.inflate(inflater, container, false)
    }
}
