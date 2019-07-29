package be.ledfan.geojsondsl

import com.beust.klaxon.JsonArray
import com.beust.klaxon.json
import geojsondsl.ICoordinate

class Ring {

    private val coordinates = ArrayList<ICoordinate>()

    fun withCoordinate(cord: ICoordinate) {
        coordinates.add(cord)
    }

    fun ensureValid() {
        if (coordinates.size < 4) {
            throw InvalidRing("Invalid Ring: should contain at least four coordinates, contains: $coordinates")
        }

        if (coordinates.first() != coordinates.last()) {
            throw InvalidRing("Invalid Ring: first and last coordinate should be equal, first is : ${coordinates.first()}, last is: ${coordinates.last()}")
        }
        // "A linear ring MUST follow the right-hand rule with respect to the
        //  area it bounds, i.e., exterior rings are counterclockwise, and
        //  holes are clockwise."
        // is not checked. TODO
    }

    fun toJson(): JsonArray<Any?> {
        ensureValid()

        return json {
            array(coordinates.map { it.toJson() })
        }
    }

}