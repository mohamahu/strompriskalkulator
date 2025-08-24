package com.example.team38.data

import kotlinx.serialization.Serializable

@Serializable
data class FrostData (
    val data: List<FrostInfo>
)


@Serializable
data class FrostInfo (
    val referenceTime: String,
    val observations: List<Observation>
)



@Serializable
data class Geometry (
    val type: String,
    val coordinates: List<Float>
)

@Serializable
data class Observation (
    val value: Float
)

