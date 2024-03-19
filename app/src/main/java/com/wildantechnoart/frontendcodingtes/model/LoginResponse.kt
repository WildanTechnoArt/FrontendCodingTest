package com.wildantechnoart.frontendcodingtes.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("statusCode") var statusCode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null,
    @SerializedName("data") var data: DataLogin? = DataLogin()
)

data class DataLogin(
    @SerializedName("token") var token: String? = null
)