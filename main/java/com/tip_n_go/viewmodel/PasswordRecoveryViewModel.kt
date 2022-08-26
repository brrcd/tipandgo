package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.state.UiState

class PasswordRecoveryViewModel : BaseViewModel() {

    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    // todo finish
}
