package com.example.team38.data

import kotlinx.serialization.Serializable

@Serializable
data class Properties (
    val timeseries: List<Time>
)