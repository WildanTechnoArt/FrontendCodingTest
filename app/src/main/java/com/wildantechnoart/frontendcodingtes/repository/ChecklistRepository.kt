package com.wildantechnoart.frontendcodingtes.repository

import com.wildantechnoart.frontendcodingtes.model.AddChecklistBody
import com.wildantechnoart.frontendcodingtes.network.BaseApiService
import kotlinx.coroutines.flow.flow

class ChecklistRepository(private val baseApi: BaseApiService) {

    fun getChecklist(token: String?) = flow {
        val response = baseApi.getChecklist(token)
        emit(response)
    }

    fun deleteChecklist(token: String?, id: String?) = flow {
        val response = baseApi.deleteChecklist(token, id)
        emit(response)
    }

    fun addChecklist(token: String?, body: AddChecklistBody?) = flow {
        val response = baseApi.addChecklist(token, body)
        emit(response)
    }
}