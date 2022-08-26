package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.OrganizationUser
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.data.outgoing.DistributeTipsRequest
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class TipDistributionViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _organizationUsersLiveData =
        MutableLiveData<UiState<List<OrganizationUser>>>()
    val organizationUsersLiveData: LiveData<UiState<List<OrganizationUser>>> get() = _organizationUsersLiveData
    private val _organizationUndistributedTips = MutableLiveData<UiState<List<Tip>>>()
    val organizationUndistributedTips: LiveData<UiState<List<Tip>>> get() = _organizationUndistributedTips
    private val _distributeTipsLiveData = MutableLiveData<UiState<Unit>>()
    val distributedTipsLiveData: LiveData<UiState<Unit>> get() = _distributeTipsLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getInfo(organizationHash: String) {
        launchOnIo {
            getOrganizationUndistributedTips(organizationHash)
        }
    }

    private suspend fun getOrganizationUsers(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationUsersByHash(organizationHash)
            _organizationUsersLiveData.postValue(UiState.Success(data))
        }
    }

    private suspend fun getOrganizationUndistributedTips(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationUndistributedTips(organizationHash)
            _organizationUndistributedTips.postValue(UiState.Success(data))
        }
        getOrganizationUsers(organizationHash)
    }

    fun distributeTips(
        organizationHash: String,
        selectedTips: List<Tip>,
        selectedEmployee: List<OrganizationUser>
    ) {
        var amountTotal = 0
        val tipHashes = mutableListOf<String>()
        val userHashes = mutableListOf<String>()
        selectedEmployee.map { user -> user.hash?.let { userHashes.add(it) } }
        selectedTips.map { tip ->
            tip.amount?.let { amountTotal += (it * 100).toInt() }
            tip.hash?.let { tipHashes.add(it) }
        }
        launchOnIo {
            repository.distributeTips(
                DistributeTipsRequest(
                    organizationHash,
                    tipHashes,
                    userHashes,
                    amountTotal
                )
            )
            _distributeTipsLiveData.postValue(UiState.Success(Unit))
        }
    }
}
