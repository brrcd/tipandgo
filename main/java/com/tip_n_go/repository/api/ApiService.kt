package com.tip_n_go.repository.api

import com.google.gson.Gson
import com.tip_n_go.data.exception.ResponseError

open class ApiService : BaseService() {

    override fun parseCustomError(
        responseCode: Int,
        responseMessage: String,
        errorBodyJson: String?
    ): Exception {
        return if (!errorBodyJson.isNullOrEmpty()) {
            Gson().fromJson(errorBodyJson, ResponseError::class.java)
        } else {
            ResponseError(responseCode, responseMessage, null)
        }
    }
}
