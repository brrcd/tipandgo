package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Settings
import com.tip_n_go.data.incoming.TipDistributionMethod
import com.tip_n_go.data.incoming.UnitResponseResult
import com.tip_n_go.data.incoming.UpdateOrganizationSettings
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class SettingsViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _tipsDistributionMethodsLiveData =
        MutableLiveData<UiState<List<TipDistributionMethod>>>()
    val tipsDistributionMethodsLiveData: LiveData<UiState<List<TipDistributionMethod>>>
        get() = _tipsDistributionMethodsLiveData
    private val _settingsLiveData = MutableLiveData<UiState<List<Settings>>>()
    val settingsLiveData: LiveData<UiState<List<Settings>>> get() = _settingsLiveData
    private val _settingsUpdateSuccess = MutableLiveData<UiState<UnitResponseResult>>()
    val settingsUpdateSuccess: LiveData<UiState<UnitResponseResult>> get() = _settingsUpdateSuccess
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    init {
        getTipsDistributionMethods()
    }

    private fun getTipsDistributionMethods() {
        launchOnIo {
            val data = repository.getTipsDistributionMethods()
            _tipsDistributionMethodsLiveData.postValue(UiState.Success(data))
        }
    }

    fun getOrganizationSettings(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationSettings(organizationHash)
            _settingsLiveData.postValue(UiState.Success(data))
        }
    }

    fun updateOrganizationSettings(organizationHash: String, settings: UpdateOrganizationSettings) {
        launchOnIo {
            val data = repository.updateOrganizationSettings(organizationHash, settings)
            _settingsLiveData.postValue(UiState.Success(data))
            _settingsUpdateSuccess.postValue(
                UiState.Success(
                    UnitResponseResult(
                        settingsUpdateSuccess = true
                    )
                )
            )
        }
    }
}
