package com.tip_n_go.data.incoming

data class OrganizationBrief(
    val organization: Organization? = null,
    val organizationRole: Role? = null,
    val position: String? = ""
)
