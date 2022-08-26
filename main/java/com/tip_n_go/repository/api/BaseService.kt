package com.tip_n_go.repository.api

import com.tip_n_go.data.exception.HttpBaseException
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.state.ResponseState
import retrofit2.Response

abstract class BaseService {

    protected abstract fun parseCustomError(
        responseCode: Int,
        responseMessage: String,
        errorBodyJson: String?
    ): Exception

    protected suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): ResponseState<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            println(t)
            return ResponseState.Error(
                mapNetworkThrowable(t)
            )
        }

        return if (!response.isSuccessful) {
            val errorBody = response.errorBody()
            @Suppress("BlockingMethodInNonBlockingContext")
            ResponseState.Error(
                parseCustomError(
                    response.code(),
                    response.message(),
                    errorBody?.string() ?: ""
                )
            )
        } else {
            return if (response.body() == null) {
                ResponseState.Error(Exception("response body is null"))
            } else {
                ResponseState.Success(response.body()!!)
            }
        }
    }

    private fun mapNetworkThrowable(throwable: Throwable): ResponseError {
        val message = throwable.message ?: "network exception"
        return ResponseError(0, message, null)
    }

    protected fun mapHttpThrowable(
        throwable: Throwable,
        code: Int,
        message: String
    ): HttpBaseException {
        return HttpBaseException(throwable, code, message)
    }
}
