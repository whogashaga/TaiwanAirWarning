package com.kerry.ubiquitiassignment.repository

import com.kerry.ubiquitiassignment.network.ApiResult
import com.kerry.ubiquitiassignment.network.ApiResult.*
import com.kerry.ubiquitiassignment.model.Record
import com.kerry.ubiquitiassignment.network.ApiService
import javax.inject.Inject

class MyRepository @Inject constructor(private val retrofit: ApiService) {

    suspend fun getAirDataResult(limit: Int, apiKey: String): ApiResult<List<Record?>> {
        val deferred = retrofit.getAirDataDeferred(
            limit = limit.toString(),
            apiKey = apiKey,
        )

        return runCatching {
            val records: List<Record?>? = deferred.await().records
            if (records.isNullOrEmpty()) {
                Fail("Records is Null or Empty.")
            } else {
                Success(records)
            }
        }.getOrElse {
            Error(Exception(it))
        }

    }
}