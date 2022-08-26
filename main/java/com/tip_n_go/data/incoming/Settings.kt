package com.tip_n_go.data.incoming

data class Settings(
    val key: String? = "",
    val name: String? = "",
    val description: String? = "",
    val options: List<Option>? = null
)
