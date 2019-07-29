package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
import geojsondsl.ICoordinate

open class MultiPoint : Geometry {

    override val coordinates = ArrayList<ICoordinate>()

    override val type = "MultiPoint"

    override fun toJson(): JsonObject {
        return json {
            obj(
                    "type" to type,
                    "coordinates" to array(
                            coordinates.map { it.toJson() }
                    )
            )
        }
    }

    fun withCoordinate(cord: ICoordinate) {
        coordinates.add(cord)
    }

}
