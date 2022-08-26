package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class StaffListViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _organizationUsersLiveData =
        MutableLiveData<UiState<List<OrganizationUser>>>()
    val organizationUsersLiveData: LiveData<UiState<List<OrganizationUser>>> get() = _organizationUsersLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getOrganizationUsers(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationUsersByHash(organizationHash)
            _organizationUsersLiveData.postValue(UiState.Success(data))
        }
    }
}
