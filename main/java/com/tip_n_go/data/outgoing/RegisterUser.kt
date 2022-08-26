package com.tip_n_go.data.outgoing

class RegisterUser(
    val phone: String? = "",
    val email: String? = "",
    val lastName: String? = "",
    val firstName: String? = "",
    val countryId: Int? = 0,
    val language: String? = "",
    val code: String? = "",
    val password: String? = "",
    val userKey: String? = "",
    val agreeWithPersonalDataHandling: Boolean? = false,
    val emailConfirmationNeeded: Boolean? = false
)
