package com.example.walker.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// DTO to create a new walk request
data class WalkRequestCreate(
    val originLat: Double,
    val originLng: Double,
    val destLat: Double? = null,
    val destLng: Double? = null,
    val preferredTime: String? = null
)

// DTO representing a walker (simplified, match your backend)
data class WalkerDto(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double
)

interface WalkApi {
    @POST("walk-requests")
    suspend fun createRequest(@Body req: WalkRequestCreate): Response<Unit>

    @GET("walkers")
    suspend fun getNearbyWalkers(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double
    ): Response<List<WalkerDto>>
}
