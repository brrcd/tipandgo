package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.data.incoming.QrCodeData
import com.tip_n_go.data.outgoing.QrCodeRequest
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class QrCodeViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _qrCodeLiveData = MutableLiveData<UiState<QrCodeData>>()
    val qrCodeLiveData: LiveData<UiState<QrCodeData>> get() = _qrCodeLiveData
    private val _organizationUsersLiveData =
        MutableLiveData<UiState<List<OrganizationUser>>>()
    val organizationUsersLiveData: LiveData<UiState<List<OrganizationUser>>>
        get() = _organizationUsersLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getQrCode(
        employeeHash: String,
        organizationHash: String,
        tableNo: Int,
        invoiceTotal: Double
    ) {
        launchOnIo {
            val total = (invoiceTotal * 100).toInt()
            val data = repository.getQrCode(
                QrCodeRequest(
                    employeeHash, organizationHash, tableNo, total
                )
            )
            _qrCodeLiveData.postValue(UiState.Success(data))
        }
    }

    fun getOrganizationUsersByHash(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationUsersByHash(organizationHash)
            _organizationUsersLiveData.postValue(UiState.Success(data))
        }
    }
}
