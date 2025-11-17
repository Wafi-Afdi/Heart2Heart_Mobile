package com.example.heart2heart.contacts.data.remote

import com.example.heart2heart.BuildConfig
import com.example.heart2heart.contacts.data.AddContactReqDTO
import com.example.heart2heart.auth.domain.model.ListOfContactModelResponse
import com.example.heart2heart.contacts.data.dto.AddUserAccesDTO
import com.example.heart2heart.contacts.data.dto.UserResponseResDto
import com.example.heart2heart.contacts.domain.model.DeleteContactRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

interface ContactAPI {
    @GET("api/access/user")
    suspend fun getListOfContacts(
        @Query("isAmbulatory") isAmbulatory: Boolean
    ): ListOfContactModelResponse

    @DELETE("api/access/user")
    suspend fun deleteContact(
        @Query("userId") userId: UUID,
    ): String

    @POST("api/access/user")
    suspend fun addContact(
        @Body addUserAccesDTO: AddUserAccesDTO
    ): UserResponseResDto

    companion object {
        const val BASE_URL = BuildConfig.BE_API_LOCAL
    }
}