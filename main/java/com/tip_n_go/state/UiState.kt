package com.tip_n_go.state

import com.tip_n_go.data.exception.ResponseError

sealed class UiState<out R : Any> {

    class Success<out T : Any>(val data: T) : UiState<T>()
    class Error(val exception: ResponseError) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}
