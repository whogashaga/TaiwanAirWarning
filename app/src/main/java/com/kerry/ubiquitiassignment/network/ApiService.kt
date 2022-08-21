package com.kerry.ubiquitiassignment.network

import com.kerry.ubiquitiassignment.model.AirDataResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("aqx_p_432?limit=1000&api_key=58f8c0e0-d154-4d6d-b062-54001d8fa192&sort=ImportDate%20desc&format=json")
    fun getAirData(
        @Query("limit") limit: String,
        @Query("api_key") apiKey: String,
        @Query("sort") sort: String,
        @Query("format") format: String
    ): Call<AirDataResult>

// https://data.epa.gov.tw/api/v2/aqx_p_432?limit=1000&api_key=58f8c0e0-d154-4d6d-b062-54001d8fa192&sort=ImportDate%20desc&format=json

}