package com.tip_n_go.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ecommpay.sdk.ECMPActivity
import com.ecommpay.sdk.ECMPPaymentInfo
import com.tip_n_go.R
import com.tip_n_go.data.incoming.*
import com.tip_n_go.databinding.FragmentEditProfileBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.ui.adapter.BankCardAdapter
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.EditProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(),
    PopupMenu.OnMenuItemClickListener,
    TopBarChangerInterface {

    private val viewModel: EditProfileViewModel by viewModel()
    private var image: File? = null

    private val cameraRequestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { success ->
            if (success) {
                launchGetPhoto()
            } else {
                binding.saveButton.showSnack(getString(R.string.permission_denied))
            }
        }

    private val photoPickerResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val file = FileTools.createFileFromUri(requireContext(), uri)
                viewModel.uploadPhoto(file)
            }
        }

    private val photoCaptureResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                if (image != null) {
                    FileTools.resizeImageFile(requireContext(), image!!)
                    viewModel.uploadPhoto(image!!)
                }
            }
        }

    private val addCardResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                ECMPActivity.RESULT_SUCCESS -> {
                    viewModel.getUserBankCards()
                }
                ECMPActivity.RESULT_CANCELLED -> {
                    binding.saveButton.showSnack(getString(R.string.operation_canceled))
                }
                ECMPActivity.RESULT_DECLINE -> {
                    binding.saveButton.showSnack(getString(R.string.operation_declined))
                }
                ECMPActivity.RESULT_FAILED -> {
                    binding.saveButton.showSnack(getString(R.string.operation_failed))
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.bankCardsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.userLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.photoLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.ecommpayLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.updateUserLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getUserBankCards()
        viewModel.getUser()
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<BankCard> { setupBankCardsRecycler(it) }
                if (data is User) {
                    setupInfo(data)
                }
                if (data is UrlResponse) {
                    setPhoto(data)
                }
                if (data is EcommpayConfig) {
                    openEcommpay(data)
                }
                if (data is UnitResponseResult) {
                    if (data.updateUser) {
                        binding.saveButton.showSnack(getString(R.string.profile_updated))
                    }
                }
            }
            is UiState.Error -> {
                uiState.showError(binding.saveButton)
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupView() = with(binding) {
        photoLayout.setOnClickListener {
            showPhotoSelector()
        }
        saveButton.setOnClickListener {
            val email = email.getText
            val firstName = firstName.getText
            val lastName = lastName.getText
            viewModel.updateUser(email, firstName, lastName)
        }
    }

    private fun showPhotoSelector() {
        val popup = PopupMenu(requireContext(), binding.profilePhoto)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.photo_picker)
        popup.show()
    }

    private fun setPhoto(url: UrlResponse) {
        GlideApp.with(requireContext())
            .load(url.url)
            .into(binding.profilePhoto)
    }

    private fun setupInfo(user: User) {
        val lastName = user.lastName
        val firstName = user.firstName
        val email = user.email
        val avatar = user.avatar
        val phone = user.phone

        binding.lastName.editText?.setText(lastName)
        binding.firstName.editText?.setText(firstName)
        binding.email.editText?.setText(email)
        if (phone != null){
            binding.phone.editText.setText(phone)
        }

        if (!avatar.isNullOrEmpty()) {
            GlideApp.with(requireContext())
                .load(avatar)
                .into(binding.profilePhoto)
            binding.photoText.text = getString(R.string.change_photo)
        }
        binding.addCard.setOnClickListener {
            viewModel.getEcommpay()
        }
    }

    private fun setupBankCardsRecycler(list: List<BankCard>) {
        val adapter = BankCardAdapter()
        binding.cardsRecycler.adapter = adapter
        adapter.updateList(list)
    }

    private fun launchGetPhoto() {
        image = FileTools.createImageFile(requireContext())
        val imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            image!!
        )
        photoCaptureResult.launch(imageUri)
    }

    private fun openEcommpay(config: EcommpayConfig) {
        val info = ECMPPaymentInfo(
            config.project_id!!,
            config.payment_id,
            config.payment_amount!!,
            config.payment_currency,
            config.payment_description,
            config.customer_id,
            config.region_code
        )
        info.signature = config.signature
        info.action = ECMPPaymentInfo.ActionType.Verify
        info.languageCode = config.language_code
        info.addEcmpScreenDisplayMode("hide_success_final_page")
            .addEcmpScreenDisplayMode("hide_decline_final_page")
        info.hideSavedWallets = config.hide_saved_wallets == 1
        addCardResult.launch(Intent(ECMPActivity.buildIntent(requireContext(), info)))
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.camera -> {
                val cameraPermission = Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(requireContext(), cameraPermission)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    cameraRequestPermission.launch(Manifest.permission.CAMERA)
                } else {
                    launchGetPhoto()
                }
                return true
            }
            R.id.gallery -> {
                photoPickerResult.launch(INTENT_IMAGE_TYPE)
                return true
            }
        }
        return false
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditProfileBinding {
        return FragmentEditProfileBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = getString(R.string.edit_profile)
        mainActivity.binding.openNotifications.gone()
    }

    override fun restoreDefaultTopBar() {}
}
