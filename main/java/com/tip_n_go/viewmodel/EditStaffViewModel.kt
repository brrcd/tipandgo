package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Role
import com.tip_n_go.data.outgoing.RegisterOrganizationUser
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class EditStaffViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _rolesLiveData = MutableLiveData<UiState<List<Role>>>()
    val rolesLiveData: LiveData<UiState<List<Role>>> get() = _rolesLiveData
    private val _userLiveData = MutableLiveData<UiState<Unit>>()
    val userLiveData: LiveData<UiState<Unit>> get() = _userLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getAvailableRoles(organizationHash: String) {
        launchOnIo {
            val data = repository.getAvailableRoles(organizationHash)
            _rolesLiveData.postValue(UiState.Success(data))
        }
    }

    fun registerUserToOrganization(
        phone: String,
        email: String,
        lastName: String,
        firstName: String,
        organizationHash: String,
        position: String,
        roleId: Int
    ) {
        launchOnIo {
            val data = repository.registerUserToOrganization(
                RegisterOrganizationUser(
                    phone,
                    email,
                    lastName,
                    firstName,
                    organizationHash,
                    position,
                    roleId
                )
            )
            _userLiveData.postValue(UiState.Success(data))
        }
    }
}
