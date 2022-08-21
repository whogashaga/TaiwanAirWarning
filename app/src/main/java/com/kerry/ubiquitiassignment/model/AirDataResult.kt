package com.kerry.ubiquitiassignment.model

import com.google.gson.annotations.SerializedName

data class AirDataResult(
    @SerializedName("__extras")
    val extras: Extras? = Extras(),
    @SerializedName("_links")
    val links: Links? = Links(),
    val fields: List<Field?>? = listOf(),
    @SerializedName("include_total")
    val includeTotal: Boolean? = false,
    val limit: String? = "",
    val offset: String? = "",
    val records: List<Record?>? = listOf(),
    @SerializedName("resource_format")
    val resourceFormat: String? = "",
    @SerializedName("resource_id")
    val resourceId: String? = "",
    val total: String? = ""
)