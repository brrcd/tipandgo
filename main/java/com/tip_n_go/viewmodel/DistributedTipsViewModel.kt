package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.DistributedTip
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class DistributedTipsViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _distributedTipsLiveData = MutableLiveData<UiState<List<DistributedTip>>>()
    val distributedTipsLiveData: LiveData<UiState<List<DistributedTip>>> get() = _distributedTipsLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getDistributedTips(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationDistributedTips(organizationHash)
            _distributedTipsLiveData.postValue(UiState.Success(data))
        }
    }
}
