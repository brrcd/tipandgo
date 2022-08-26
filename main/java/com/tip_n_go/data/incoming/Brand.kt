package com.tip_n_go.data.incoming

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand(
    val hash: String? = "",
    val name: String? = "",
    val logo: String? = ""
) : Parcelable
