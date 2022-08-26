package com.tip_n_go.data.incoming

data class Token(
    val accessToken: String = "",
    val accessTokenLifetime: Int = 0,
    val refreshToken: String = "",
    val refreshTokenLifetime: Int = 0
)
