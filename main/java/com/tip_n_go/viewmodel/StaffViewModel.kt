package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.ChartType
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.*
import com.tip_n_go.data.outgoing.Chart
import com.tip_n_go.data.outgoing.ReportDataRequest
import com.tip_n_go.data.outgoing.UpdateOrganizationUser
import com.tip_n_go.data.outgoing.UserInOrganizationRequest
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.CURRENT_ORGANIZATION_HASH
import com.tip_n_go.tools.CURRENT_USER_HASH
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.get

class StaffViewModel(private val repository: DataSource) : BaseViewModel() {

    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData
    private val _tipsLiveData = MutableLiveData<UiState<List<Tip>>>()
    val tipsLiveData: LiveData<UiState<List<Tip>>> get() = _tipsLiveData
    private val _reviewsLiveData = MutableLiveData<UiState<List<Review>>>()
    val reviewsLiveData: LiveData<UiState<List<Review>>> get() = _reviewsLiveData
    private val _rolesLiveData = MutableLiveData<UiState<List<Role>>>()
    val rolesLiveData: LiveData<UiState<List<Role>>> get() = _rolesLiveData
    private val _userLiveData = MutableLiveData<UiState<User>>()
    val userLiveData: LiveData<UiState<User>> get() = _userLiveData
    private val _tipReportsLiveData = MutableLiveData<UiState<ReportData>>()
    val tipReportsLiveData: LiveData<UiState<ReportData>> get() = _tipReportsLiveData
    private val _reviewReportsLiveData = MutableLiveData<UiState<ReportData>>()
    val reviewReportsLiveData: LiveData<UiState<ReportData>> get() = _reviewReportsLiveData

    fun getTips(userHash: String, organizationHash: String) {
        launchOnIo {
            val data =
                repository.getOrganizationUserTips(PeriodType.DAY, userHash, organizationHash)
            _tipsLiveData.postValue(UiState.Success(data))
        }
    }

    fun getReviews(userHash: String, organizationHash: String) {
        launchOnIo {
            val data =
                repository.getOrganizationUserReviews(
                    PeriodType.DAY,
                    userHash,
                    organizationHash
                )
            _reviewsLiveData.postValue(UiState.Success(data))
        }
    }

    fun getAvailableRoles(organizationHash: String) {
        launchOnIo {
            val data = repository.getAvailableRoles(organizationHash)
            _rolesLiveData.postValue(UiState.Success(data))
        }
    }

    fun updateUser(userHash: String, organizationHash: String, position: String, roleId: Int) {
        launchOnIo {
            val data =
                repository.updateOrganizationUser(
                    UpdateOrganizationUser(
                        userHash, organizationHash, position, roleId
                    )
                )
            _userLiveData.postValue(UiState.Success(data))
        }
    }

    fun getUserInOrganization(organizationUserHash: String, organizationHash: String) {
        launchOnIo {
            val data = repository.getUserInOrganization(
                UserInOrganizationRequest(
                    organizationUserHash, organizationHash
                )
            )
            _userLiveData.postValue(UiState.Success(data))
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
                            ChartType.UserTips
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
                            ChartType.UserReviews
                        )
                    )
                )
            )
            _reviewReportsLiveData.postValue(UiState.Success(data.last()))
        }
    }
}
