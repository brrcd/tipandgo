package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Organization
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class OrganizationsViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _organizationsLiveData = MutableLiveData<UiState<List<Organization>>>()
    val organizationLiveData: LiveData<UiState<List<Organization>>> get() = _organizationsLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getUserOrganizations() {
        launchOnIo {
            val data = repository.getUserOrganizations()
            _organizationsLiveData.postValue(UiState.Success(data))
        }
    }
}
