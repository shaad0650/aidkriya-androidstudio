package com.example.walker.repository

import com.example.walker.network.ApiProvider
import com.example.walker.network.LoginReq
import com.example.walker.network.LoginRes
import com.example.walker.network.RegisterReq
import com.example.walker.storage.TokenStorage

class AuthRepository(
    private val tokenStorage: TokenStorage
) {
    private val api = ApiProvider.authApi

    suspend fun register(email: String, password: String, role: String? = "wanderer") {
        val res = api.register(RegisterReq(email, password, role))
        if (!res.isSuccessful) throw Exception("Register failed: ${res.code()} ${res.errorBody()?.string()}")
    }

    suspend fun login(email: String, password: String): LoginRes {
        val res = api.login(LoginReq(email, password))
        if (!res.isSuccessful) throw Exception("Login failed: ${res.code()} ${res.errorBody()?.string()}")
        val body = res.body() ?: throw Exception("Empty response")
        // persist token + update in-memory holder
        tokenStorage.saveToken(body.access_token)
        ApiProvider.apply { NetworkModule.TokenHolder.accessToken = body.access_token }
        return body
    }
}
