package com.example.team38.data
import kotlinx.serialization.Serializable

@Serializable
data class ForecastData(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)