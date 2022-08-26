package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.ChartType
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.*
import com.tip_n_go.data.outgoing.Chart
import com.tip_n_go.data.outgoing.PayoutUser
import com.tip_n_go.data.outgoing.RemoveFcmToken
import com.tip_n_go.data.outgoing.ReportDataRequest
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.CURRENT_ORGANIZATION_HASH
import com.tip_n_go.tools.CURRENT_USER_HASH
import com.tip_n_go.tools.FCM_TOKEN
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.get

class ProfileViewModel(private val repository: DataSource) : BaseViewModel() {

    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData
    private val _balanceLiveData = MutableLiveData<UiState<Balance>>()
    val balanceLiveData: LiveData<UiState<Balance>> get() = _balanceLiveData
    private val _bankCardsLiveData = MutableLiveData<UiState<List<BankCard>>>()
    val bankCardsLiveData: LiveData<UiState<List<BankCard>>> get() = _bankCardsLiveData
    private val _payoutSuccessLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val payoutSuccessLiveData: LiveData<UiState<UnitResponseResult>> get() = _payoutSuccessLiveData
    private val _removeFcmTokenLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val removeFcmTokenLiveData: LiveData<UiState<UnitResponseResult>> get() = _removeFcmTokenLiveData
    private val _tipReportsLiveData = MutableLiveData<UiState<ReportData>>()
    val tipReportsLiveData: LiveData<UiState<ReportData>> get() = _tipReportsLiveData
    private val _reviewReportsLiveData = MutableLiveData<UiState<ReportData>>()
    val reviewReportsLiveData: LiveData<UiState<ReportData>> get() = _reviewReportsLiveData
    private val _notificationsLiveData = MutableLiveData<UiState<List<Notification>>>()
    val notificationLiveData: LiveData<UiState<List<Notification>>> get() = _notificationsLiveData

    fun getBalance() {
        launchOnIo {
            val data = repository.getUserBalance()
            _balanceLiveData.postValue(UiState.Success(data))
        }
    }

    fun getUserBankCards() {
        launchOnIo {
            val data = repository.getUserBankCards()
            _bankCardsLiveData.postValue(UiState.Success(data))
        }
    }

    fun payoutUser(amount: Double, bankCardHash: String) {
        launchOnIo {
            val intAmount = (amount * 100).toInt()
            val payout = PayoutUser(
                intAmount, bankCardHash
            )
            val data = repository.payoutUser(payout)
            _balanceLiveData.postValue(UiState.Success(data))
            _payoutSuccessLiveData.postValue(UiState.Success(UnitResponseResult(payoutSuccess = true)))
        }
    }

    fun removeFcmToken() {
        launchOnIo {
            val fcmToken = SharedTools.sharedPrefs()[FCM_TOKEN] ?: ""
            repository.removeFcmToken(
                RemoveFcmToken(
                    fcmToken
                )
            )
            _removeFcmTokenLiveData.postValue(
                UiState.Success(
                    UnitResponseResult(
                        removeFcmTokenSuccess = true
                    )
                )
            )
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
                            ChartType.CurrentTips
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
                            ChartType.CurrentReviews
                        )
                    )
                )
            )
            _reviewReportsLiveData.postValue(UiState.Success(data.last()))
        }
    }

    fun getUserNotifications() {
        launchOnIo {
            val data = repository.getUserNotifications()
            _notificationsLiveData.postValue(UiState.Success(data))
        }
    }
}
