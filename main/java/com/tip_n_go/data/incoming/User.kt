package com.tip_n_go.data.incoming

import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.UserDeserializer

@JsonAdapter(UserDeserializer::class)
data class User(
    val hash: String? = "",
    val lastName: String? = "",
    val firstName: String? = "",
    val phone: String? = "",
    val email: String? = "",
    val emailConfirmed: Boolean? = false,
    val language: String? = "",
    val avatar: String? = "",
    val rating: Double? = 0.0,
    val userRoles: List<Role>? = null,
    val userOrganizations: List<OrganizationBrief>? = null,
    val currency: Currency? = null
) {
    val fullName: String
        get() {
            return if (lastName.isNullOrBlank() && !firstName.isNullOrBlank()) {
                firstName
            } else if (firstName.isNullOrBlank() && !lastName.isNullOrBlank()) {
                lastName
            } else {
                "$lastName $firstName"
            }
        }
}
