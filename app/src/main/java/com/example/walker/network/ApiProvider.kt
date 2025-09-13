package com.example.walker.network

object ApiProvider {
    val authApi: AuthApi = NetworkModule.retrofit.create(AuthApi::class.java)
    val walkApi: WalkApi = NetworkModule.retrofit.create(WalkApi::class.java)
}
