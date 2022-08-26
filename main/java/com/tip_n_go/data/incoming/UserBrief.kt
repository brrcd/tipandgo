package com.tip_n_go.data.incoming

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.UserBriefDeserializer
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonAdapter(UserBriefDeserializer::class)
data class UserBrief(
    val hash: String? = "",
    val lastName: String? = "",
    val firstName: String? = "",
    val phone: String? = "",
    val email: String? = "",
    val emailConfirmed: Boolean? = false,
    val language: String? = "",
    val avatar: String? = "",
    val rating: Double? = 0.0,
    val position: String? = ""
) : Parcelable {
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
