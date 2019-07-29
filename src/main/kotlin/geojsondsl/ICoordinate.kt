package geojsondsl

import com.beust.klaxon.JsonArray

interface ICoordinate {

    fun toJson(): JsonArray<Double>

}

