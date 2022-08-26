package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Payout
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class WithdrawHistoryViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _payoutHistoryLiveData = MutableLiveData<UiState<List<Payout>>>()
    val payoutHistoryLiveData: LiveData<UiState<List<Payout>>> get() = _payoutHistoryLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    init {
        getPayoutHistory()
    }

    private fun getPayoutHistory() {
        launchOnIo {
            val data = repository.getUserPayoutHistory()
            _payoutHistoryLiveData.postValue(UiState.Success(data))
        }
    }
}
