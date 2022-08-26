package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.ChartType
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Balance
import com.tip_n_go.data.incoming.Organization
import com.tip_n_go.data.incoming.ReportData
import com.tip_n_go.data.incoming.Tip
import com.tip_n_go.data.outgoing.Chart
import com.tip_n_go.data.outgoing.ReportDataRequest
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.CURRENT_ORGANIZATION_HASH
import com.tip_n_go.tools.CURRENT_USER_HASH
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.get

class DashboardViewModel(private val repository: DataSource) : BaseViewModel() {

    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData
    private val _organizationLiveData = MutableLiveData<UiState<Organization>>()
    val organizationLiveData: LiveData<UiState<Organization>> get() = _organizationLiveData
    private val _organizationBalance = MutableLiveData<UiState<Balance>>()
    val organizationBalance: LiveData<UiState<Balance>> get() = _organizationBalance
    private val _organizationUndistributedTips = MutableLiveData<UiState<List<Tip>>>()
    val organizationUndistributedTips: LiveData<UiState<List<Tip>>> get() = _organizationUndistributedTips
    private val _tipReportsLiveData = MutableLiveData<UiState<ReportData>>()
    val tipReportsLiveData: LiveData<UiState<ReportData>> get() = _tipReportsLiveData
    private val _reviewReportsLiveData = MutableLiveData<UiState<ReportData>>()
    val reviewReportsLiveData: LiveData<UiState<ReportData>> get() = _reviewReportsLiveData

    fun getOrganizationByHash(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationByHash(organizationHash)
            _organizationLiveData.postValue(UiState.Success(data))
        }
    }

    fun getOrganizationUndistributedTips(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationUndistributedTips(organizationHash)
            _organizationUndistributedTips.postValue(UiState.Success(data))
        }
    }

    fun getOrganizationBalance(organizationHash: String) {
        launchOnIo {
            val data = repository.getOrganizationBalance(organizationHash)
            _organizationBalance.postValue(UiState.Success(data))
        }
    }

    fun getTipReportsData(period: PeriodType) {
        launchOnIo {
            val data = repository.getReportsData(
                ReportDataRequest(
                    listOf(
                        Chart(
                            SharedTools.sharedPrefs()[CURRENT_USER_HASH],
                            SharedTools.sharedPrefs()[CURRENT_ORGANIZATION_HASH],
                            period,
                            ChartType.OrganizationTips
                        ),
                        Chart()
                    )
                )
            )
            _tipReportsLiveData.postValue(UiState.Success(data.first()))
        }
    }

    fun getReviewReportsData(period: PeriodType) {
        launchOnIo {
            val data = repository.getReportsData(
                ReportDataRequest(
                    listOf(
                        Chart(),
                        Chart(
                            SharedTools.sharedPrefs()[CURRENT_USER_HASH],
                            SharedTools.sharedPrefs()[CURRENT_ORGANIZATION_HASH],
                            period,
                            ChartType.OrganizationReviews
                        )
                    )
                )
            )
            _reviewReportsLiveData.postValue(UiState.Success(data.last()))
        }
    }
}
