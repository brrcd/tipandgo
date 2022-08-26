package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Token
import com.tip_n_go.data.outgoing.RefreshToken
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class LandingViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _refreshJwtTokenLiveData = MutableLiveData<UiState<Token>>()
    val refreshJwtTokenLiveData: LiveData<UiState<Token>> get() = _refreshJwtTokenLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun refreshToken(token: Token) {
        launchOnIo {
            val data = repository.refreshJwtToken(RefreshToken(token.refreshToken))
            _refreshJwtTokenLiveData.postValue(UiState.Success(data))
        }
    }
}
