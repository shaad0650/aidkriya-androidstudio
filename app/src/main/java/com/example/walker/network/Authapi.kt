package com.example.walker.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Data classes (DTOs) for requests and responses
data class RegisterReq(
    val email: String,
    val password: String,
    val role: String? = "wanderer" // default role if not passed
)

data class LoginReq(
    val email: String,
    val password: String
)

data class LoginRes(
    val access_token: String
)

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterReq): Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body body: LoginReq): Response<LoginRes>
}
