package com.tip_n_go.data.incoming

import com.google.gson.annotations.JsonAdapter
import com.tip_n_go.tools.UpdateSettingsSerializer

@JsonAdapter(UpdateSettingsSerializer::class)
data class UpdateSettings(
    val key: String? = "",
    val selectedOptionId: List<Int>? = null
)
