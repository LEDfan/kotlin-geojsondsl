package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject

interface Geometry {
    fun toJson(): JsonObject

    val type: String
    val coordinates: Any

}