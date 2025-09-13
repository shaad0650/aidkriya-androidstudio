package com.example.walker.repository

import com.example.walker.network.ApiProvider
import com.example.walker.network.WalkRequestCreate
import com.example.walker.network.WalkerDto

class WalkRepository {
    private val api = ApiProvider.walkApi

    suspend fun createWalkRequest(
        originLat: Double,
        originLng: Double,
        destLat: Double? = null,
        destLng: Double? = null,
        preferredTime: String? = null
    ) {
        val res = api.createRequest(WalkRequestCreate(originLat, originLng, destLat, destLng, preferredTime))
        if (!res.isSuccessful) {
            throw Exception("Walk request failed: ${res.code()} ${res.errorBody()?.string()}")
        }
    }

    suspend fun getNearbyWalkers(lat: Double, lng: Double): List<WalkerDto> {
        val res = api.getNearbyWalkers(lat, lng)
        if (!res.isSuccessful) {
            throw Exception("Failed to fetch walkers: ${res.code()} ${res.errorBody()?.string()}")
        }
        return res.body() ?: emptyList()
    }
}
