package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.CURRENT_ORGANIZATION_HASH
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.get

class TipsViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _organizationTipsLiveData = MutableLiveData<UiState<List<Tip>>>()
    val organizationTipsLiveData: LiveData<UiState<List<Tip>>> get() = _organizationTipsLiveData
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun update() {
        val orgHash = sharedPrefs[CURRENT_ORGANIZATION_HASH] ?: ""
        getOrganizationTips(organizationHash = orgHash)
    }

    private fun getOrganizationTips(
        period: PeriodType = PeriodType.DAY,
        organizationHash: String,
    ) {
        launchOnIo {
            val data = repository.getOrganizationTips(period, organizationHash)
            _organizationTipsLiveData.postValue(UiState.Success(data))
        }
    }
}
