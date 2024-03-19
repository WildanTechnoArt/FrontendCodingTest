package com.wildantechnoart.frontendcodingtes.model

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("username") var username: String? = null
)