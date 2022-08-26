package com.tip_n_go.data.exception

class HttpBaseException(val throwable: Throwable, val code: Int, val errorMessage: String) : BaseApiException()
