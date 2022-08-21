package com.kerry.ubiquitiassignment.network

import com.kerry.ubiquitiassignment.model.AirDataResult
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("aqx_p_432?sort=ImportDate%20desc&format=json")
    fun getAirDataDeferred(
        @Query("limit") limit: String,
        @Query("api_key") apiKey: String,
    ): Deferred<AirDataResult>

}