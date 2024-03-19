package com.wildantechnoart.frontendcodingtes.model

import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("password") var password: String? = null,
    @SerializedName("username") var username: String? = null
)