package com.tip_n_go.data.exception

import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.ResponseErrorDeserializer
import com.tip_n_go.tools.ValidationErrorsDeserializer

@JsonAdapter(ResponseErrorDeserializer::class)
class ResponseError(
    val code: Int,
    val error: String,
    val validationErrors: ValidationErrors?
) : BaseApiException()

@JsonAdapter(ValidationErrorsDeserializer::class)
class ValidationErrors(
    var additionalProp1: String = "",
    var additionalProp2: String = "",
    var additionalProp3: String = ""
)
