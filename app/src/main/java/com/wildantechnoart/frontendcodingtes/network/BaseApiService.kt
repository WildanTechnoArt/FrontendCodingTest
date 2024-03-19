package com.wildantechnoart.frontendcodingtes.network

import com.wildantechnoart.frontendcodingtes.model.AddChecklistBody
import com.wildantechnoart.frontendcodingtes.model.ItemsResponse
import com.wildantechnoart.frontendcodingtes.model.LoginBody
import com.wildantechnoart.frontendcodingtes.model.LoginResponse
import com.wildantechnoart.frontendcodingtes.model.RegisterBody
import com.wildantechnoart.frontendcodingtes.model.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BaseApiService {

    @Headers("Accept: application/json")
    @POST("register")
    suspend fun register(
        @Body body: RegisterBody?
    ): Response

    @Headers("Accept: application/json")
    @POST("login")
    suspend fun login(
        @Body body: LoginBody?
    ): LoginResponse


    @Headers("Accept: application/json")
    @GET("checklist")
    suspend fun getChecklist(
        @Header("Authorization") token: String?
    ): ItemsResponse

    @Headers("Accept: application/json")
    @POST("checklist")
    suspend fun addChecklist(
        @Header("Authorization") token: String?,
        @Body body: AddChecklistBody?
    ): Response

    @Headers("Accept: application/json")
    @DELETE("checklist/{id}")
    suspend fun deleteChecklist(
        @Header("Authorization") token: String?,
        @Path("id") id: String?
    ): Response
}