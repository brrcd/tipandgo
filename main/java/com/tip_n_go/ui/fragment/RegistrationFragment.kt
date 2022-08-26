package com.tip_n_go.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Country
import com.tip_n_go.data.incoming.Token
import com.tip_n_go.data.incoming.UnitResponseResult
import com.tip_n_go.data.outgoing.RegisterUser
import com.tip_n_go.databinding.FragmentRegistrationBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.ui.activity.MainActivity
import com.tip_n_go.ui.listener.ArrowBackInterface
import com.tip_n_go.ui.listener.CodeInputInterface
import com.tip_n_go.ui.listener.CustomTabSelectedListener
import com.tip_n_go.ui.listener.CustomTextWatcher
import com.tip_n_go.viewmodel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(),
    CodeInputInterface,
    ArrowBackInterface {

    private val viewModel: RegistrationViewModel by viewModel()
    private var currentTabId = 0
    private var countries: List<Country> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkEmailLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.checkPhoneLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getCountriesLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.registerUserLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { renderData(it) }
        setupView()
        showEmailTab()
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                if (data is UnitResponseResult) {
                    if (data.emailCheck || data.phoneCheck) {
                        showCodeInput(true)
                        setApproveButton(true)
                    }
                } else if (data is Token) {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    this@RegistrationFragment.requireActivity().finish()
                }
                data.listCastCheck<Country> { setupCountrySelector(it) }
                binding.loadingFrame.loading.gone()
            }
            is UiState.Error -> {
                val exception = uiState.exception
                val message = exception.error
                binding.approveButton.showSnack(message)
                binding.loadingFrame.loading.gone()
            }
            is UiState.Loading -> {
                binding.loadingFrame.loading.visible()
            }
        }
    }

    private fun setupView() {
        showCodeInput(false)
        setOnArrowBackPressed()
        setupTabLayout()
        showEmailTab()
    }

    private fun setupTabLayout() {
        fillWithTabs()
        setListener()
    }

    private fun showEmailTab() = with(binding) {
        phoneInput.editText.text?.clear()
        emailInput.visible()
        phoneInput.gone()
        passwordInput.hint = getString(R.string.password)
        passwordInput.editText?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        passwordInput.visible()
        setApproveTextWatcher()
        setApproveButton(false, EMAIL)
    }

    private fun showPhoneTab() = with(binding) {
        emailInput.editText?.text?.clear()
        emailInput.gone()
        phoneInput.visible()
        passwordInput.gone()
        setApproveTextWatcher(false)
        setApproveButton(false, PHONE)
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

    private fun setListener() = with(binding) {
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
                passwordInput.editText?.text?.clear()
                passwordInput.editText?.clearFocus()
                codeInput.editText?.text?.clear()
                codeInput.editText?.clearFocus()
                phoneInput.editText.text?.clear()
                showCodeInput(false)
            }
        })
    }

    override fun showCodeInput(show: Boolean): Unit = with(binding) {
        if (show) {
            codeInput.visible()
            if (currentTabId == PHONE) {
                codeInput.requestFocus()
            }
            approveButton.text = getString(R.string.approve_registration)
            approveButton.isEnabled = false
            setApproveTextWatcher(true)
        } else {
            codeInput.gone()
            approveButton.text = getString(R.string.receive_code_action)
        }
    }

    private fun setupCountrySelector(countries: List<Country>) {
        this.countries = countries
        val mList = countries.map { it.name }.toMutableList()
        val adapter = createHintAdapter(mList)
        binding.countrySelector.adapter = adapter
        binding.countrySelector.setSelection(adapter.count)
        setOnCountrySelected(countries)
    }

    private fun setOnCountrySelected(countries: List<Country>) {
        binding.countrySelector.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, v: View?, index: Int, p3: Long) {
                if (index < countries.size) {
                    binding.countryHint.gone()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setApproveButton(isCodeReceived: Boolean, tabId: Int? = PHONE) {
        binding.approveButton.setOnClickListener {
            if (isCodeReceived) {
                approveRegistration()
            } else {
                if (tabId == EMAIL) {
                    receiveCodeForEmail()
                } else {
                    receiveCodeForPhone()
                }
            }
        }
    }

    private fun receiveCodeForEmail() {
        val email = binding.emailInput.editText?.text.toString()
        viewModel.checkEmail(email)
    }

    private fun receiveCodeForPhone() {
        val phone = filterPhoneNumber()
        viewModel.checkPhone(phone)
    }

    private fun filterPhoneNumber(): String {
        return binding.phoneInput.editText.text.toString().filter { it.isDigit() }
    }

    private fun approveRegistration() {
        val selectedCountry = binding.countrySelector.selectedItem.toString()
        if (selectedCountry.isBlank()) {
            // todo resources
            binding.approveButton.showSnack("Select country")
            return
        }
        val firstName = binding.firstNameInput.editText?.text.toString()
        val countryId: Int? = countries.first { it.name == selectedCountry }.id
        val language: String = Locale.getDefault().language
        val code: String = binding.codeInput.editText?.text.toString()
        val user = when (currentTabId) {
            EMAIL -> {
                val email = binding.emailInput.editText?.text
                    .toString()
                    .filter { !it.isWhitespace() }
                val password = binding.passwordInput.editText?.text.toString()
                RegisterUser(
                    email = email,
                    firstName = firstName,
                    countryId = countryId,
                    language = language,
                    code = code,
                    password = password
                )
            }
            PHONE -> {
                val phone = filterPhoneNumber()
                RegisterUser(
                    phone = phone,
                    firstName = firstName,
                    countryId = countryId,
                    language = language,
                    code = code
                )
            }
            else -> {
                RegisterUser()
            }
        }
        viewModel.registerUser(user)
    }

    private fun setApproveTextWatcher(codeReceived: Boolean = false) = with(binding) {
        val emailInputListener = object : CustomTextWatcher {
            override fun afterTextChanged(input: Editable?) {
                approveButton.isEnabled =
                    !(input.isNullOrBlank() || passwordInput.editText?.text.isNullOrBlank())
            }
        }
        val phoneInputListener = object : CustomTextWatcher {
            override fun afterTextChanged(input: Editable?) {
                approveButton.isEnabled = !input.isNullOrBlank()
            }
        }
        val passwordInputListener = object : CustomTextWatcher {
            override fun afterTextChanged(input: Editable?) {
                approveButton.isEnabled =
                    !(input.isNullOrBlank() || emailInput.editText?.text.isNullOrBlank())
            }
        }
        val codeInputListener = object : CustomTextWatcher {
            override fun afterTextChanged(input: Editable?) {
                approveButton.isEnabled =
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
                codeInput.editText?.addTextChangedListener(codeInputListener)
            } else {
                phoneInput.editText.addTextChangedListener(phoneInputListener)
            }
        }
    }

    override fun setOnArrowBackPressed() {
        binding.arrowBack.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater, container, false)
    }

    companion object {
        private const val EMAIL = 0
        private const val PHONE = 1
    }
}
