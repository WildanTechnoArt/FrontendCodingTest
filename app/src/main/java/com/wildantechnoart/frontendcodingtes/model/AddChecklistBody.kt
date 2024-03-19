package com.wildantechnoart.frontendcodingtes.model

import com.google.gson.annotations.SerializedName

data class AddChecklistBody(
    @SerializedName("name") var name: String? = null
)