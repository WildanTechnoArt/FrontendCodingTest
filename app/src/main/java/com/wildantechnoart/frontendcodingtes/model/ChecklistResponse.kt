package com.wildantechnoart.frontendcodingtes.model

import com.google.gson.annotations.SerializedName

data class ChecklistResponse(
    @SerializedName("statusCode") var statusCode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null,
    @SerializedName("data") var data: ArrayList<ChecklistData> = arrayListOf()
)

data class ChecklistData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("items") var items: String? = null,
    @SerializedName("checklistCompletionStatus") var checklistCompletionStatus: Boolean? = null
)