package com.wildantechnoart.frontendcodingtes.repository

import com.wildantechnoart.frontendcodingtes.model.LoginBody
import com.wildantechnoart.frontendcodingtes.model.RegisterBody
import com.wildantechnoart.frontendcodingtes.network.BaseApiService
import kotlinx.coroutines.flow.flow

class AuthRepository(private val baseApi: BaseApiService) {

    fun register(body: RegisterBody) = flow {
        val response = baseApi.register(body)
        emit(response)
    }

    fun login(body: LoginBody) = flow {
        val response = baseApi.login(body)
        emit(response)
    }
}