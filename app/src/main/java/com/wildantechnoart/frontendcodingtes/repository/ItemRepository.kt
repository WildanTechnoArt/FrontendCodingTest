package com.wildantechnoart.frontendcodingtes.repository

import com.wildantechnoart.frontendcodingtes.model.ItemBody
import com.wildantechnoart.frontendcodingtes.network.BaseApiService
import kotlinx.coroutines.flow.flow

class ItemRepository(private val baseApi: BaseApiService) {

    fun getItems(token: String?, checklistId: String?) = flow {
        val response = baseApi.getItems(token, checklistId)
        emit(response)
    }

    fun postItems(token: String?, checklistId: String?, body: ItemBody) = flow {
        val response = baseApi.postItems(token, checklistId, body)
        emit(response)
    }

    fun deleteItems(token: String?, checklistId: String?, itemId: String?) = flow {
        val response = baseApi.deleteItems(token, checklistId, itemId)
        emit(response)
    }

    fun updateItems(token: String?, checklistId: String?, itemId: String?) = flow {
        val response = baseApi.updateItems(token, checklistId, itemId)
        emit(response)
    }

    fun renameItems(token: String?, checklistId: String?, itemId: String?, body: ItemBody) = flow {
        val response = baseApi.renameItems(token, checklistId, itemId, body)
        emit(response)
    }
}