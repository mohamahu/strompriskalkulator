package com.example.team38.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class StromprisData(
    @SerializedName("NOK_per_kWh") val NOK_per_kWh: Double,
    @SerializedName("EUR_per_kWh") val EUR_per_kWh: Double,
    @SerializedName("EXR") val EXR: Double,
    @SerializedName("time_start") val time_start: String,
    @SerializedName("time_end") val time_end: String
)
