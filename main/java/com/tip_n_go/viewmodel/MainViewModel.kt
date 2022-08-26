package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Organization
import com.tip_n_go.data.incoming.User
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.tools.SharedTools.set

class MainViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _userLiveData = MutableLiveData<UiState<User>>()
    val userLiveData: LiveData<UiState<User>> get() = _userLiveData
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    private var organizationsMap = LinkedHashMap<String, Organization>()
    internal var organizationsTitles = listOf<String>()
    internal var userName = ""
    private val _organizationHashLiveData = MutableLiveData<UiState<Unit>>()
    val organizationHashLiveData: LiveData<UiState<Unit>> get() = _organizationHashLiveData
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun getUser() {
        launchOnIo {
            val data = repository.getUser()
            fillOrganizationsMap(data)
            userName = getUserName(data)
            sharedPrefs[CURRENT_USER_HASH] = data.hash
            sharedPrefs[CURRENT_USER] = data
            sharedPrefs[USER_LANGUAGE] = data.language
            if (!data.userOrganizations.isNullOrEmpty()) {
                if (data.userOrganizations[0].organization != null) {
                    sharedPrefs[CURRENT_ORGANIZATION_HASH] =
                        data.userOrganizations.first().organization?.hash
                    _organizationHashLiveData.postValue(UiState.Success(Unit))
                }
            }
            _userLiveData.postValue(UiState.Success(data))
        }
    }

    private fun fillOrganizationsMap(user: User) {
        if (user.userOrganizations != null) {
            user.userOrganizations.forEach { userOrg ->
                if (userOrg.organization != null) {
                    userOrg.organization.hash?.let {
                        organizationsMap[it] = userOrg.organization
                    }
                }
            }
        }
        organizationsTitles = organizationsMap.map { it.value.name!! }
    }

    private fun getUserName(user: User): String {
        return if (user.lastName.isNullOrBlank() && !user.firstName.isNullOrBlank()) {
            user.firstName
        } else if (user.firstName.isNullOrBlank() && !user.lastName.isNullOrBlank()) {
            user.lastName
        } else {
            "${user.lastName} ${user.firstName}"
        }
    }

    fun selectCurrentOrganizationHash(position: Int) {
        sharedPrefs[CURRENT_ORGANIZATION_HASH] = organizationsMap.keys.elementAt(position)
    }
}
