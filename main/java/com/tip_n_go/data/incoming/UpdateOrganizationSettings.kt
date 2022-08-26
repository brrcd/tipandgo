package com.tip_n_go.data.incoming

import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.UpdateOrganizationSettingsSerializer

@JsonAdapter(UpdateOrganizationSettingsSerializer::class)
data class UpdateOrganizationSettings(
    val settings: List<UpdateSettings>? = null
)
