package com.tip_n_go.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.BuildConfig
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Token
import com.tip_n_go.data.incoming.UnitResponseResult
import com.tip_n_go.data.outgoing.CheckPhone
import com.tip_n_go.data.outgoing.RegisterFcmToken
import com.tip_n_go.data.outgoing.TokenRequest
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.FCM_TOKEN
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.tools.SharedTools.set
import com.tip_n_go.tools.TOKEN

class LoginViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _jwtTokenLiveData = MutableLiveData<UiState<Token>>()
    val jwtTokenLiveData: LiveData<UiState<Token>> get() = _jwtTokenLiveData
    private val _checkPhoneLiveData = MutableLiveData<UiState<Unit>>()
    val checkPhoneLiveData: LiveData<UiState<Unit>> get() = _checkPhoneLiveData
    private val _fcmLiveData = MutableLiveData<UiState<UnitResponseResult>>()
    val fcmLiveData: LiveData<UiState<UnitResponseResult>> get() = _fcmLiveData
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getJwtToken(
        phone: String = "",
        code: String = "",
        email: String = "",
        password: String = ""
    ) {
        launchOnIo {
            val tokenRequest = TokenRequest(phone, code, email, password)
            val data = repository.getJwtToken(tokenRequest)
            _jwtTokenLiveData.postValue(UiState.Success(data))
            sharedPrefs[TOKEN] = data
        }
    }

    fun checkPhone(phone: String) {
        _checkPhoneLiveData.value = UiState.Loading
        launchOnIo {
            val fcmToken: String? = sharedPrefs[FCM_TOKEN]
            val data = repository.checkPhone(CheckPhone(phone, fcmToken))
            _checkPhoneLiveData.postValue(UiState.Success(data))
        }
    }

    fun registerFcmToken() {
        launchOnIo {
            val appVersion = BuildConfig.APP_VERSION
            val vendor = Build.MANUFACTURER
            val model = Build.MODEL
            val os = "Android ${Build.VERSION.RELEASE} API ${Build.VERSION.SDK_INT}"
            val name = getPhoneName()
            repository.registerFcmToken(
                RegisterFcmToken(
                    sharedPrefs[FCM_TOKEN] ?: "",
                    appVersion,
                    vendor,
                    model,
                    os,
                    name
                )
            )
        }
    }

    private fun getPhoneName(): String {
        return "Device"
    }
}
