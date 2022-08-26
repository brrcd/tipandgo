package com.tip_n_go.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tip_n_go.R
import com.tip_n_go.databinding.FragmentDialogPasswordRecoveryBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.ui.listener.DialogCloseButtonInterface
import com.tip_n_go.viewmodel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordRecoveryDialogFragment :
    BottomSheetDialogFragment(),
    DialogCloseButtonInterface {

    private var _binding: FragmentDialogPasswordRecoveryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegistrationViewModel by viewModel()

    override fun getTheme() = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogPasswordRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        // todo FINISH
        viewModel.passwordRecoveryLiveData.observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupView() {
        setupCloseButton()
    }

    override fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
