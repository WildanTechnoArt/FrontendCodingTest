package com.wildantechnoart.frontendcodingtes.model

import com.google.gson.annotations.SerializedName

data class ItemBody(
    @SerializedName("itemName") var itemName: String? = null
)