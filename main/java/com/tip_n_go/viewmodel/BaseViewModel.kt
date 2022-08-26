package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected val _errorLiveData = MutableLiveData<UiState<ResponseError>>()
    abstract val errorLiveData: LiveData<UiState<ResponseError>>

    // each call inside viewModel implementation should be wrapper in this function
    // to be able to catch error
    protected suspend fun <T : Any> safeCall(call: suspend () -> T): T? {
        try {
            return call()
        } catch (e: Throwable) {
            parseError(e)
        }
        return null
    }

    protected fun <T : Any> launchOnIo(call: suspend () -> T){
        viewModelScope.launch(Dispatchers.IO) {
            safeCall(call)
        }
    }

    private fun parseError(e: Throwable) {
        _errorLiveData.postValue(UiState.Error(e as ResponseError))
    }
}
