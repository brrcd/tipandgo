package com.tip_n_go.data.incoming

import android.os.Parcelable
import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.OrganizationDeserializer
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonAdapter(OrganizationDeserializer::class)
data class Organization(
    val brand: Brand? = null,
    val name: String? = "",
    val legalName: String? = "",
    val phone: String? = "",
    val website: String? = "",
    val email: String? = "",
    val address: String? = "",
    val hash: String? = "",
    val tipDistributionMethodId: Int? = 0,
    val rating: Double? = 0.0,
    val country: Country? = null,
    val currency: Currency? = null,
    val organizationUsers: List<OrganizationUser>? = null
) : Parcelable
