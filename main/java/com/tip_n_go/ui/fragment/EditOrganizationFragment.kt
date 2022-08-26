package com.tip_n_go.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isNotEmpty
import androidx.navigation.fragment.findNavController
import com.google.android.material.textview.MaterialTextView
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Brand
import com.tip_n_go.data.incoming.Country
import com.tip_n_go.data.incoming.Organization
import com.tip_n_go.data.incoming.UnitResponseResult
import com.tip_n_go.databinding.FragmentEditOrganizationBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.EditOrganizationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditOrganizationFragment :
    BaseFragment<FragmentEditOrganizationBinding>(),
    TopBarChangerInterface {

    private val viewModel: EditOrganizationViewModel by viewModel()
    private var brandsList = listOf<Brand>()
    private var countryList = listOf<Country>()
    private var isFromDashboard = false // is needed to turn back our top bar
    private var isNewBrandSelected = false
    private var isEditing = false
    private var organizationHash = ""
    private var selectedBrand = Brand()

    private val photoPickerResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val file = FileTools.createFileFromUri(requireContext(), uri)
                val brandHash = selectedBrand.hash
                if (brandHash != null) {
                    viewModel.uploadLogo(brandHash, file)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        organizationHash = arguments?.getString(ORGANIZATION_HASH) ?: ""
        isFromDashboard = findNavController().previousBackStackEntry?.destination?.id ==
                R.id.dashboardFragment
        isEditing = organizationHash.isNotBlank()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userBrandsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getCountriesLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.organizationLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.brandLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.createBrandLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.createOrgLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.updateOrgLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { renderData(it) }
        if (isEditing) {
            viewModel.getOrganization(organizationHash)
        } else {
            viewModel.getUserBrands()
            viewModel.getCountries()
        }
        setupView()
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                if (data is Organization) {
                    setupOrganization(data)
                }
                if (data is Brand) {
                    selectedBrand = data
                    loadBrandLogo(selectedBrand.logo)
                }
                if (data is UnitResponseResult) {
                    if (data.createBrandSuccess || data.updateBrandSuccess) {
                        if (organizationHash.isNotBlank()) {
                            updateOrganization()
                        } else {
                            createOrganization()
                        }
                    } else if (data.createOrganizationSuccess) {
                        binding.saveButton.showSnack("Organization created")
                    } else if (data.updateOrganizationSuccess) {
                        binding.saveButton.showSnack("Organization updated")
                    }
                }
                data.listCastCheck<Brand> {
                    setupBrandSelector(it)
                    brandsList = it
                }
                data.listCastCheck<Country> {
                    setupCountrySelector(it)
                    countryList = it
                }
            }
            is UiState.Error -> {
                uiState.showError(binding.saveButton)
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupView() {
        if (organizationHash.isNotBlank()) {
            binding.organizationPhone.gone()
        }
        binding.saveButton.setOnClickListener {
            if (isNewBrandSelected) {
                // if brand creation is success then creates organization
                createBrand()
            } else if (isEditing) {
                updateOrganization()
            } else {
                createOrganization()
            }
        }
        binding.logoLayout.setOnClickListener {
            photoPickerResult.launch(INTENT_IMAGE_TYPE)
        }
        setBrandNameEditDone()
    }

    private fun setBrandNameEditDone(){
        binding.brandSelector.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updateBrand()
            }
            true
        }
    }

    private fun setupOrganization(data: Organization) {
        val brandName = data.brand?.name
        val brandLogo = data.brand?.logo
        val organizationName = data.name
        val legalName = data.legalName
        val email = data.email
        val phone = data.phone
        val website = data.website
        val address = data.address

        selectedBrand = data.brand ?: Brand()
        binding.organizationName.editText?.setText(organizationName)
        binding.organizationLegalEntityName.editText?.setText(legalName)
        binding.organizationEmail.editText?.setText(email)
        binding.organizationWebsite.editText?.setText(website)
        binding.organizationAddress.editText?.setText(address)
        binding.brandSelector.setText(brandName, false)
        binding.organizationBrandEdit.visible()
        binding.countryTab.gone()
        if (phone != null) {
            binding.organizationPhone.editText.setText(phone)
        }
        loadBrandLogo(brandLogo)
    }

    private fun loadBrandLogo(brandLogo: String?) {
        if (brandLogo.isNullOrBlank()) {
            val placeholderRes = getDrawable(R.drawable.logo_placeholder)
            GlideApp.with(binding.root)
                .load(placeholderRes)
                .into(binding.organizationLogo)
            binding.logoText.text = getString(R.string.upload_logo_sign)
        } else {
            GlideApp.with(binding.root)
                .load(brandLogo)
                .into(binding.organizationLogo)
            binding.logoText.text = getString(R.string.change_logo)
        }
    }

    private fun updateBrand(){
        val name = binding.brandSelector.text.toString()
        val brand = selectedBrand.copy(name = name)
        viewModel.updateBrand(brand)
    }

    // todo redo
    @SuppressLint("ClickableViewAccessibility")
    private fun setupBrandSelector(brands: List<Brand>) = with(binding) {
        val newBrand = context?.getString(R.string.new_brand_name)
        val brandNames: MutableList<String?> = brands.map { it.name }.toMutableList()
        brandNames.add(newBrand)
        val namesArray = brandNames.toTypedArray()
        brandSelector.setSimpleItems(namesArray)
        brandSelector.setOnTouchListener { view, _ ->
            // show all brands on textview touch
            (view as AutoCompleteTextView?)?.showDropDown()
            this@EditOrganizationFragment.hideKeyboard()
            true
        }
        brandSelector.setOnItemClickListener { _, view, index, _ ->
            // if New Brand selected show New Brand name tab and hide edit brand selector edit image
            isNewBrandSelected = if ((view as MaterialTextView).text.toString() == newBrand) {
                organizationNewBrandName.visible()
                organizationBrandEdit.gone()
                true
            } else {
                organizationNewBrandName.gone()
                organizationBrandEdit.visible()
                selectedBrand = brands[index]
                loadBrandLogo(selectedBrand.logo)
                false
            }
        }
        if (organizationNewBrandName.isNotEmpty()) {
            brandSelector.setText(organizationNewBrandName.getText, false)
            organizationNewBrandName.gone()
            isNewBrandSelected = false
        }

        // on brand selector edit image click, focusing on current brand selected to edit
        // show keyboard and setting on touch so u can select text again
        organizationBrandEdit.setOnClickListener {
            brandSelector.requestFocus()
            brandSelector.showKeyboard()
            brandSelector.setOnTouchListener { _, _ -> false }
        }
    }

    private fun setupCountrySelector(countries: List<Country>) {
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

    private fun createBrand() {
        val brandName = binding.organizationNewBrandName.editText?.text.toString()
        if (brandName.isBlank()) {
            binding.saveButton.showSnack(getString(R.string.fill_brand_name))
        } else {
            viewModel.createBrand(brandName)
        }
    }

    private fun updateOrganization() {
        val currentSelectedBrand = binding.brandSelector.text.toString()
        val brandHash = if (isNewBrandSelected) {
            selectedBrand.hash ?: ""
        } else {
            brandsList.first { it.name == currentSelectedBrand }.hash ?: ""
        }
        val name = binding.organizationName.getText
        val legalName = binding.organizationLegalEntityName.getText
        val phone = filterPhoneNumber()
        val website = binding.organizationWebsite.getText
        val email = binding.organizationEmail.getText
        val address = binding.organizationAddress.getText
        viewModel.updateOrganization(
            organizationHash,
            brandHash,
            name,
            legalName,
            phone,
            website,
            email,
            address
        )
    }

    private fun createOrganization() {
        val currentSelectedBrand = binding.brandSelector.text.toString()
        val brandHash = if (isNewBrandSelected) {
            selectedBrand.hash ?: ""
        } else if (currentSelectedBrand.isBlank()) {
            // todo resources & translate
            binding.saveButton.showSnack("Select brand")
            return
        } else {
            brandsList.first { it.name == currentSelectedBrand }.hash ?: ""
        }
        val name = binding.organizationName.getText
        val legalName = binding.organizationLegalEntityName.getText
        val phone = filterPhoneNumber()
        val website = binding.organizationWebsite.getText
        val email = binding.organizationEmail.getText
        val address = binding.organizationAddress.getText
        val currentlySelectedCountry = binding.countrySelector.selectedItem
        if (currentlySelectedCountry.toString().isBlank()) {
            // todo resources & translate
            binding.saveButton.showSnack("Select country")
            return
        }
        val countryId = countryList.first { it.name == currentlySelectedCountry }.id ?: 0
        viewModel.createOrganization(
            brandHash,
            name,
            legalName,
            phone,
            website,
            email,
            address,
            countryId
        )
    }

    private fun filterPhoneNumber(): String {
        return binding.organizationPhone.editText.text.toString().filter { it.isDigit() }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditOrganizationBinding {
        return FragmentEditOrganizationBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = if (organizationHash.isBlank()) {
            getString(R.string.add_new_organization)
        } else {
            //todo translate
            getString(R.string.edit_organization)
        }
    }

    override fun restoreDefaultTopBar() {
        if (isFromDashboard) {
            mainActivity.onReturnFromFragment()
        }
    }
}
