package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json

class MultiPolygon : Geometry {

    override val coordinates = ArrayList<Polygon>()

    override val type = "MultiPolygon"

    fun polygon(polygon: Polygon.() -> Unit = {}) {
        val r = Polygon()
        r.apply(polygon)
        coordinates.add(r)
    }

    override fun toJson(): JsonObject {
        return json {
            obj(
                    "type" to type,
                    "coordinates" to array(
                            coordinates.map { polygon ->
                                polygon.coordinates.map {
                                    ring -> ring.toJson()
                                }
                            }
                    )
            )
        }
    }

}
