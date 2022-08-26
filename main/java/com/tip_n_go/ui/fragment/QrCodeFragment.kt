package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.text.isDigitsOnly
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tip_n_go.R
import com.tip_n_go.data.incoming.OrganizationBrief
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.data.incoming.QrCodeData
import com.tip_n_go.data.incoming.User
import com.tip_n_go.databinding.FragmentQrCodeBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.QrCodeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class QrCodeFragment : BaseFragment<FragmentQrCodeBinding>(), TopBarChangerInterface {

    private val viewModel: QrCodeViewModel by viewModel()
    private var employeeHash = ""
    private var organizationHash = ""
    private var currentUser by Delegates.notNull<User>()
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentUser = sharedPrefs[CURRENT_USER, User()]!!
        employeeHash = arguments?.getString(ORGANIZATION_USER_HASH) ?: ""
        organizationHash = arguments?.getString(ORGANIZATION_HASH) ?: ""
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.qrCodeLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.organizationUsersLiveData.observe(viewLifecycleOwner) { renderData(it) }
        setupViews()
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                if (data is QrCodeData) {
                    generateQrCode(data.data!!)
                }
                data.listCastCheck<OrganizationUser> { setupUserSelector(it) }
            }
            is UiState.Error -> {
                val error = uiState.exception.error
                binding.generateQrButton.showSnack(error)
            }
            is UiState.Loading -> {
            }
        }
    }

    // todo SELECTOR ISSUE
    // WHEN TRYING TO SELECT EMPLOYEE OR ORGANIZATION WHEN THERE IS NOTHING TO SELECT
    // TABLE N AND TOTAL INPUT BREAKS
    private fun setupViews() = with(binding) {
        if (organizationHash.isNotBlank() && employeeHash.isNotBlank()){
            organizationLayout.gone()
            employeeLayout.gone()
        }
        generateQrButton.setOnClickListener {
            if (organizationHash.isBlank()) {
                organizationHash = sharedPrefs[CURRENT_ORGANIZATION_HASH, ""] ?: ""
            }
            if (employeeHash.isBlank()) {
                employeeHash = sharedPrefs[CURRENT_USER_HASH, ""] ?: ""
            }
            val tableNumber: Int =
                if (!tableNumberInput.editText?.text.isNullOrBlank()) {
                    if (tableNumberInput.editText?.text!!.toString().isDigitsOnly()) {
                        tableNumberInput.editText?.text.toString().toInt()
                    } else {
                        -1
                    }
                } else {
                    -1
                }
            val invoiceTotal =
                if (!invoiceTotalInput.editText?.text.isNullOrBlank()) {
                    if (!invoiceTotalInput.editText?.text!!.toString().toDouble().isNaN()) {
                        invoiceTotalInput.editText?.text.toString().toDouble()
                    } else {
                        -1.0
                    }
                } else {
                    -1.0
                }
            if (tableNumber == -1 && invoiceTotal == -1.0) {
                generateQrButton.showSnack(getString(R.string.fill_requested_fields))
            } else {
                viewModel.getQrCode(
                    employeeHash,
                    organizationHash,
                    tableNumber,
                    invoiceTotal
                )
            }
        }
        val organizationsList = currentUser.userOrganizations
        if (organizationsList != null) {
            setupOrganizationSelector(organizationsList)
        }
    }

    private fun setupOrganizationSelector(list: List<OrganizationBrief>) {
        when (list.size) {
            0 -> {
                binding.organizationSelector.gone()
            }
            1 -> {
                binding.organizationHint.text = list[0].organization?.name
            }
            else -> {
                val mList = list.map { it.organization?.name }.toMutableList()
                val adapter = createHintAdapter(mList)
                binding.organizationSelector.adapter = adapter
                binding.organizationSelector.setSelection(adapter.count)
                setOnOrganizationSelected(list)
            }
        }
    }

    private fun setOnOrganizationSelected(list: List<OrganizationBrief>) = with(binding) {
        organizationSelector.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, v: View?, index: Int, p3: Long) {
                if (index < list.size) {
                    organizationHint.gone()
                    list[index].organization?.hash?.let {
                        organizationHash = it
                        viewModel.getOrganizationUsersByHash(it)
                    }
                    setupUserSelector(listOf())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setupUserSelector(list: List<OrganizationUser>) {
        when (list.size) {
            0 -> {
                binding.employeeHint.visible()
                binding.employeeSelector.gone()
                binding.employeeHint.text = getString(R.string.employee_title)
            }
            1 -> {
                binding.employeeHint.visible()
                binding.employeeSelector.gone()
                binding.employeeHint.text = list[0].firstName
            }
            else -> {
                binding.employeeSelector.visible()
                val mList = list.map { it.firstName }.toMutableList()
                val adapter = createHintAdapter(mList)
                binding.employeeSelector.adapter = adapter
                binding.employeeSelector.setSelection(adapter.count)
                setOnUserSelected(list)
            }
        }
    }

    private fun setOnUserSelected(list: List<OrganizationUser>) {
        binding.employeeSelector.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, v: View?, index: Int, p3: Long) {
                if (index < list.size) {
                    binding.employeeHint.gone()
                    employeeHash = list[index].hash ?: ""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun generateQrCode(data: String) {
        val barcodeEncoder = BarcodeEncoder()
        val barcodeSize = 2048
        val width = barcodeSize.toDp
        val height = barcodeSize.toDp
        val bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, width, height)

        GlideApp.with(requireContext())
            .load(bitmap)
            .into(binding.qrCodeImage)
        binding.qrCodeImage.visible()
        binding.inputLayout.gone()
        binding.generateQrButton.gone()
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQrCodeBinding {
        return FragmentQrCodeBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = getString(R.string.qr_code)
        mainActivity.binding.openSettings.gone()
    }

    override fun restoreDefaultTopBar() {}
}
