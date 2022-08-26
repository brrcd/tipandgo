package com.tip_n_go.state

// class to handle retrofit responses
sealed class ResponseState<out R : Any> {

    data class Success<out T : Any>(val data: T) : ResponseState<T>()
    data class Error(val exception: Exception) : ResponseState<Nothing>()
}
