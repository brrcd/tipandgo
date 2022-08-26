package com.tip_n_go.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Token
import com.tip_n_go.databinding.FragmentLoginBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.gone
import com.tip_n_go.tools.showSnack
import com.tip_n_go.tools.visible
import com.tip_n_go.ui.activity.MainActivity
import com.tip_n_go.ui.dialog.PasswordRecoveryDialogFragment
import com.tip_n_go.ui.listener.CodeInputInterface
import com.tip_n_go.ui.listener.CustomTabSelectedListener
import com.tip_n_go.ui.listener.CustomTextWatcher
import com.tip_n_go.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(),
    CodeInputInterface {

    private val viewModel: LoginViewModel by viewModel()
    private var currentTabId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.jwtTokenLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.checkPhoneLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { renderData(it) }
        setupView()
    }

    override fun renderData(uiState: UiState<Any>) = with(binding) {
        when (uiState) {
            is UiState.Success -> {
                // in case when we received phone code
                if (uiState.data is Unit) {
                    showCodeInput(true)
                    setLoginButton(EMAIL)
                    // in case of successful token retrieval
                } else if (uiState.data is Token) {
                    viewModel.registerFcmToken()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    this@LoginFragment.requireActivity().finish()
                }
                binding.loadingFrame.loading.gone()
            }
            is UiState.Error -> {
                val error = uiState.exception.error
                binding.loginButton.showSnack(error)
                binding.loadingFrame.loading.gone()
            }
            is UiState.Loading -> {
                binding.loadingFrame.loading.visible()
            }
        }
    }

    private fun setupView() {
        binding.registration.setOnClickListener {
            navigateTo(R.id.action_loginFragment_to_registrationFragment)
        }
        setupTabLayout()
        showEmailTab()
    }

    private fun showEmailTab() = with(binding) {
        phoneInput.editText.text?.clear()
        emailInput.visible()
        phoneInput.gone()
        passwordInput.hint = getString(R.string.password)
        passwordInput.editText?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        passwordInput.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        forgotPassword.text = getString(R.string.password_recovery)
        forgotPassword.setOnClickListener {
            openPasswordRecovery()
        }
        showCodeInput(true)
        setLoginButton(EMAIL)
        setLoginWithPasswordTextWatcher()
    }

    private fun showPhoneTab() = with(binding) {
        emailInput.editText?.text?.clear()
        emailInput.gone()
        phoneInput.visible()
        passwordInput.hint = getString(R.string.code)
        passwordInput.editText?.inputType = InputType.TYPE_CLASS_NUMBER
        passwordInput.endIconMode = TextInputLayout.END_ICON_NONE
        forgotPassword.text = getString(R.string.resend)
        forgotPassword.setOnClickListener {
            receiveCode()
        }
        showCodeInput(false)
        setLoginButton(PHONE)
        setLoginWithPasswordTextWatcher(false)
    }

    private fun setupTabLayout() {
        fillWithTabs()
        setTabListener()
        binding.tabLayout.root.selectTab(binding.tabLayout.root.getTabAt(EMAIL))
    }

    private fun fillWithTabs() = with(binding.tabLayout) {
        root.newTab().apply {
            text = getString(R.string.email)
            root.addTab(this)
        }
        root.newTab().apply {
            text = getString(R.string.phone)
            root.addTab(this)
        }
    }

    private fun setTabListener() = with(binding) {
        tabLayout.root.addOnTabSelectedListener(object : CustomTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTabId = tab?.position ?: 0
                when (tab?.position) {
                    EMAIL -> {
                        showEmailTab()
                    }
                    PHONE -> {
                        showPhoneTab()
                    }
                }
                tab?.position?.let { setOnActionEnterListener(it) }
                passwordInput.editText?.text?.clear()
                passwordInput.editText?.clearFocus()
            }
        })
    }

    // when login selector in phone tab hide code input
    override fun showCodeInput(show: Boolean): Unit = with(binding) {
        if (show) {
            passwordInput.visible()
            if (currentTabId == PHONE) {
                passwordInput.requestFocus()
            }
            loginButton.text = getString(R.string.login_action)
            loginButton.isEnabled = false
            setLoginWithPasswordTextWatcher(true)
        } else {
            passwordInput.gone()
            loginButton.text = getString(R.string.receive_code_action)
        }
    }

    private fun setLoginButton(currentTabId: Int) {
        binding.loginButton.setOnClickListener {
            if (currentTabId == EMAIL) {
                login()
            } else {
                receiveCode()
            }
        }
    }

    // sets soft keyboard action enter listener
    private fun setOnActionEnterListener(tabId: Int) = with(binding) {
        phoneInput.editText.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                if (tabId == EMAIL) {
                    passwordInput.editText?.requestFocus()
                } else {
                    receiveCode()
                }
                return@setOnEditorActionListener true
            } else return@setOnEditorActionListener false
        }
        passwordInput.editText?.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                login()
                return@setOnEditorActionListener true
            } else return@setOnEditorActionListener false
        }
    }

    private fun login() = with(binding) {
        if (currentTabId == EMAIL) {
            val login = emailInput.editText?.text.toString()
            val password = passwordInput.editText?.text.toString()
            viewModel.getJwtToken(email = login, password = password)
        } else {
            val phone = filterPhoneNumber()
            val code = passwordInput.editText?.text.toString()
            viewModel.getJwtToken(phone = phone, code = code)
        }
    }

    private fun filterPhoneNumber(): String {
        return binding.phoneInput.editText.text.toString().filter { it.isDigit() }
    }

    private fun receiveCode() {
        val phone = filterPhoneNumber()
        viewModel.checkPhone(phone)
    }

    private fun openPasswordRecovery() {
        val dialog = PasswordRecoveryDialogFragment()
        dialog.show(this.parentFragmentManager, "")
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    private fun setLoginWithPasswordTextWatcher(codeReceived: Boolean = false) = with(binding) {
        val emailInputListener = object : CustomTextWatcher {
            override fun afterTextChanged(input: Editable?) {
                loginButton.isEnabled =
                    !(input.isNullOrBlank() || passwordInput.editText?.text.isNullOrBlank())
            }
        }
        val phoneInputListener = object : CustomTextWatcher {
            override fun afterTextChanged(input: Editable?) {
                loginButton.isEnabled = !input.isNullOrBlank()
            }
        }
        val passwordInputListener = object : CustomTextWatcher {
            override fun afterTextChanged(input: Editable?) {
                loginButton.isEnabled =
                    !(input.isNullOrBlank() || emailInput.editText?.text.isNullOrBlank())
            }
        }
        val codeInputListener = object : CustomTextWatcher {
            override fun afterTextChanged(input: Editable?) {
                loginButton.isEnabled =
                    !(input.isNullOrBlank() || phoneInput.editText.text.isNullOrBlank())
            }
        }
        emailInput.editText?.removeTextChangedListener(emailInputListener)
        phoneInput.editText.removeTextChangedListener(phoneInputListener)
        passwordInput.editText?.removeTextChangedListener(passwordInputListener)
        passwordInput.editText?.removeTextChangedListener(codeInputListener)

        if (currentTabId == EMAIL) {
            emailInput.editText?.addTextChangedListener(emailInputListener)
            passwordInput.editText?.addTextChangedListener(passwordInputListener)
        } else {
            if (codeReceived) {
                phoneInput.editText.addTextChangedListener(phoneInputListener)
                passwordInput.editText?.addTextChangedListener(codeInputListener)
            } else {
                phoneInput.editText.addTextChangedListener(phoneInputListener)
            }
        }
    }

    companion object {
        private const val EMAIL = 0
        private const val PHONE = 1
    }
}
