package com.tip_n_go.data.incoming

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val id: Int? = 0,
    val code: String? = "",
    val sign: String? = "",
    val name: String? = ""
) : Parcelable
