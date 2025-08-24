package com.example.team38.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.json.Json

class Datasource(private val pathStrom: String, private val pathForecast: String, private val pathFrost: String){
    private val client = HttpClient(OkHttp){
        install(ContentNegotiation){
            json(Json{ignoreUnknownKeys = true})
        }
    }
    private val apiKey = "0ded4cec-81e5-4113-8d4b-1ac18ab000ec"
    suspend fun fetchStromprisData(): List<StromprisData> {
        val response = client.get(pathStrom) {
            headers {
                append("X-Gravitee-API-Key", apiKey)
            }
        }
        return response.body()
    }

    //egen datasource per api?
    suspend fun fetchForecastData(): List<Float> {
        val response = client.get(pathForecast) {
            headers {
                append("X-Gravitee-API-Key", apiKey)
            }
        }
        val jsonBody: ForecastData = response.body()
        val temp: MutableList<Float> = mutableListOf()
        for (time in jsonBody.properties.timeseries) {
            val hour = "${time.time[11]}${time.time[12]}".toInt()
            val check = listOf(0, 6, 12, 18)
            if (hour in check) {
                temp.add(time.data.instant.details.air_temperature)
            }

        }

        return temp
    }

    suspend fun fetchFrostData(): List<Float> {
        val response = client.get(pathFrost) {
            headers {
                append("X-Gravitee-API-Key", apiKey)
            }
        }
        val jsonBody: FrostData = response.body()
        val legacyTemps: MutableList<Float> = mutableListOf()
        for (data in jsonBody.data) {
            for (observation in data.observations) {
                val hour = "${data.referenceTime[11]}${data.referenceTime[12]}".toInt()
                val check = listOf(0, 6, 12, 18)
                if (hour in check) {
                    legacyTemps.add(observation.value)
                }
            }
        }
        return legacyTemps
    }
}