package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json

class MultiLineString : Geometry {

    override val coordinates = ArrayList<LineString>()

    override val type = "MultiLineString"

    fun lineString(lineString: LineString.() -> Unit = {}) {
        val r = LineString()
        r.apply(lineString)
        coordinates.add(r)
    }

    override fun toJson(): JsonObject {
        return json {
            obj(
                    "type" to type,
                    "coordinates" to array(
                            coordinates.map { linestring ->
                                linestring.coordinates.map { coordinate ->
                                    coordinate.toJson()
                                }
                            }
                    )
            )
        }
    }

}
