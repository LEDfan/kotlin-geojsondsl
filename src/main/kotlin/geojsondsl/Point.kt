package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json

class Point(coordinate: ICoordinate) : Geometry {

    override val coordinates = ArrayList<ICoordinate>()

    override val type = "Point"

    init {
        coordinates.add(coordinate)
    }

    override fun toJson(): JsonObject {
        if (coordinates.size != 1) {
            throw InvalidGeometry("Invalid Point Geometry: should contain exactly one coordinate")
        }

        return json {
            obj(
                    "type" to type,
                    "coordinates" to coordinates[0].toJson()
            )
        }
    }

}


