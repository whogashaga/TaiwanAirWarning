package com.kerry.ubiquitiassignment.model

import com.google.gson.annotations.SerializedName

data class Record(
    val aqi: String? = "",
    val co: String? = "",
    @SerializedName("co_8hr")
    val co_8hr: String? = "",
    val county: String? = "",
    val latitude: String? = "",
    val longitude: String? = "",
    val no: String? = "",
    val no2: String? = "",
    val nox: String? = "",
    val o3: String? = "",
    val o3_8hr: String? = "",
    val pm10: String? = "",
    val pm10_avg: String? = "",
    @SerializedName("pm2.5")
    val pmTwoPointFive: String? = "",
    @SerializedName("pm2.5_avg")
    val pmTwoPointFiveAvg: String? = "",
    val pollutant: String? = "",
    @SerializedName("publishtime")
    val publishTime: String? = "",
    @SerializedName("siteid")
    val siteId: String? = "",
    @SerializedName("sitename")
    val siteName: String? = "",
    val so2: String? = "",
    val so2_avg: String? = "",
    val status: String? = "",
    val wind_direc: String? = "",
    val wind_speed: String? = ""
) {

    val isStatusGood: Boolean get() = (status == "良好")

    val customStatus: String
        get() = if (status == "良好") {
            "The status is good, we want to go out to have fun"
        } else {
            status.orEmpty()
        }

}