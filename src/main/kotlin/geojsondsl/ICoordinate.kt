package be.ledfan.geojsondsl

import com.beust.klaxon.JsonArray
import com.beust.klaxon.json

interface ICoordinate {

    val lat: Double

    val lon: Double

}

fun ICoordinate.toJson(): JsonArray<Any?> {
    return json {
        array(
                lon,
                lat
        )
    }
}