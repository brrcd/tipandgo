package com.tip_n_go.data.incoming

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Role(
    val id: Int? = 0,
    val name: String? = ""
) : Parcelable
