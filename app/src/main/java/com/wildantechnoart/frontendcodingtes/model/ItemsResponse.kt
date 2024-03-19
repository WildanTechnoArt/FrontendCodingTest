package com.wildantechnoart.frontendcodingtes.model

import com.google.gson.annotations.SerializedName

data class ItemsResponse(
    @SerializedName("statusCode") var statusCode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null,
    @SerializedName("data") var data: ArrayList<ItemData> = arrayListOf()
)

data class ItemData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("items") var items: String? = null,
    @SerializedName("checklistCompletionStatus") var checklistCompletionStatus: Boolean? = null
)