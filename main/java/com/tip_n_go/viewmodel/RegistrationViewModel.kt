package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Country
import com.tip_n_go.data.incoming.Token
import com.tip_n_go.data.incoming.UnitResponseResult
import com.tip_n_go.data.outgoing.CheckEmail
import com.tip_n_go.data.outgoing.CheckPhone
import com.tip_n_go.data.outgoing.EmailPasswordRecovery
import com.tip_n_go.data.outgoing.RegisterUser
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.set
import com.tip_n_go.tools.TOKEN

class RegistrationViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _checkPhoneLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val checkPhoneLiveData: LiveData<UiState<UnitResponseResult>> get() = _checkPhoneLiveData
    private val _checkEmailLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val checkEmailLiveData: LiveData<UiState<UnitResponseResult>> get() = _checkEmailLiveData
    private val _getCountriesLiveData = MutableLiveData<UiState<List<Country>>>()
    val getCountriesLiveData: LiveData<UiState<List<Country>>> get() = _getCountriesLiveData
    private val _registerUserLiveData = MutableLiveData<UiState<Token>>()
    val registerUserLiveData: LiveData<UiState<Token>> get() = _registerUserLiveData
    private val _passwordRecoveryLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val passwordRecoveryLiveData: LiveData<UiState<UnitResponseResult>> get() = _passwordRecoveryLiveData
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    init {
        getCountries()
    }

    fun checkPhone(phone: String) {
        _checkPhoneLiveData.value = UiState.Loading
        launchOnIo {
            repository.checkPhoneRegistration(CheckPhone(phone))
            _checkPhoneLiveData.postValue(UiState.Success(UnitResponseResult(phoneCheck = true)))
        }
    }

    fun checkEmail(email: String) {
        launchOnIo {
            repository.checkEmailRegistration(CheckEmail(email))
            _checkEmailLiveData.postValue(UiState.Success(UnitResponseResult(emailCheck = true)))
        }
    }

    fun registerUser(registerUser: RegisterUser) {
        launchOnIo {
            val data = repository.registerUser(registerUser)
            _registerUserLiveData.postValue(UiState.Success(Token()))
            sharedPrefs[TOKEN] = data
        }
    }

    private fun getCountries() {
        launchOnIo {
            val data = repository.getCountries()
            _getCountriesLiveData.postValue(UiState.Success(data))
        }
    }

    fun recoverPasswordByEmail(email: String) {
        launchOnIo {
            repository.recoverPasswordByEmail(
                EmailPasswordRecovery(email)
            )
            _passwordRecoveryLiveData.postValue(
                UiState.Success(
                    UnitResponseResult(
                        passwordRecoverySuccess = true
                    )
                )
            )
        }
    }
}
