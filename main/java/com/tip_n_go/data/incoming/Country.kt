package com.tip_n_go.data.incoming

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val id: Int? = 0,
    val name: String? = "",
    val code: String? = "",
    val phoneCountryCode: Int? = 0,
    val currency: Currency? = null
) : Parcelable
