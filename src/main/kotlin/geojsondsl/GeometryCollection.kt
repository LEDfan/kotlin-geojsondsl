package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json

class GeometryCollection {

    var geometries = ArrayList<Geometry>()

    fun toJson(): JsonObject {
        return json {
            obj(
                    "type" to "GeometryCollection",
                    "geometries" to array(
                            geometries.map { it.toJson() }
                    )
            )
        }
    }
}