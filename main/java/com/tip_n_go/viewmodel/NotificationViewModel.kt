package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Notification
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState

class NotificationViewModel(val repository: DataSource) : BaseViewModel() {

    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData
    private val _notificationsLiveData = MutableLiveData<UiState<List<Notification>>>()
    val notificationLiveData: LiveData<UiState<List<Notification>>> get() = _notificationsLiveData
    private val _markAsReadLiveData = MutableLiveData<UiState<Unit>>()
    val markAsReadLiveData: LiveData<UiState<Unit>> get() = _markAsReadLiveData

    fun getUserNotifications() {
        launchOnIo {
            val data = repository.getUserNotifications()
            _notificationsLiveData.postValue(UiState.Success(data))
        }
    }

    fun markNotificationAsRead(notificationId: Int) {
        launchOnIo {
            repository.markNotificationAsRead(notificationId)
            _markAsReadLiveData.postValue(UiState.Success(Unit))
        }
    }
}
