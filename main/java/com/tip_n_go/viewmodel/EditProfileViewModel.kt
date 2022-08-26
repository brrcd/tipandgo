package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.*
import com.tip_n_go.data.outgoing.UpdateUser
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class EditProfileViewModel(private val repository: DataSource) : BaseViewModel() {

    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData
    private val _bankCardsLiveData = MutableLiveData<UiState<List<BankCard>>>()
    val bankCardsLiveData: LiveData<UiState<List<BankCard>>> get() = _bankCardsLiveData
    private val _userLiveData = MutableLiveData<UiState<User>>()
    val userLiveData: LiveData<UiState<User>> get() = _userLiveData
    private val _photoLiveData = MutableLiveData<UiState<UrlResponse>>()
    val photoLiveData: LiveData<UiState<UrlResponse>> get() = _photoLiveData
    private val _ecommpayLiveData = MutableLiveData<UiState<EcommpayConfig>>()
    val ecommpayLiveData: LiveData<UiState<EcommpayConfig>> get() = _ecommpayLiveData
    private val _updateUserLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val updateUserLiveData: LiveData<UiState<UnitResponseResult>> get() = _updateUserLiveData

    fun getUserBankCards() {
        viewModelScope.launch(Dispatchers.Default) {
            safeCall {
                val data = repository.getUserBankCards()
                _bankCardsLiveData.postValue(UiState.Success(data))
            }
        }
    }

    fun getUser() {
        viewModelScope.launch(Dispatchers.Default) {
            safeCall {
                val data = repository.getUser()
                _userLiveData.postValue(UiState.Success(data))
            }
        }
    }

    fun uploadPhoto(photo: File) {
        viewModelScope.launch(Dispatchers.Default) {
            safeCall {
                val data = repository.uploadAvatar(photo)
                _photoLiveData.postValue(UiState.Success(data))
            }
        }
    }

    fun updateUser(email: String, firstName: String, lastName: String) {
        viewModelScope.launch(Dispatchers.Default) {
            safeCall {
                val data = repository.updateUser(
                    UpdateUser(
                        email, firstName, lastName
                    )
                )
                _userLiveData.postValue(UiState.Success(data))
                _updateUserLiveData.postValue(UiState.Success(UnitResponseResult(updateUser = true)))
            }
        }
    }

    fun getEcommpay() {
        viewModelScope.launch(Dispatchers.Default) {
            safeCall {
                val language = Locale.getDefault().language
                val data = repository.getEcommpayConfig(language)
                _ecommpayLiveData.postValue(UiState.Success(data))
            }
        }
    }
}
