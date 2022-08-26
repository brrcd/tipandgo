package com.tip_n_go.data.incoming

data class UnitResponseResult(
    val phoneCheck: Boolean = false,
    val emailCheck: Boolean = false,
    val registerUserSuccess: Boolean = false,
    val registerOrganizationUser: Boolean = false,
    val checkOTP: Boolean = false,
    val updateUser: Boolean = false,
    val payoutSuccess: Boolean = false,
    val passwordRecoverySuccess: Boolean = false,
    val removeFcmTokenSuccess: Boolean = false,
    val settingsUpdateSuccess: Boolean = false,
    val createBrandSuccess: Boolean = false,
    val updateBrandSuccess: Boolean = false,
    val createOrganizationSuccess: Boolean = false,
    val updateOrganizationSuccess: Boolean = false
)
