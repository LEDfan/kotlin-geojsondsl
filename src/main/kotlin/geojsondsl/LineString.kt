package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject

class LineString: MultiPoint() {

    override val type = "LineString"

    override fun toJson(): JsonObject {
        if (coordinates.size < 2) {
            throw InvalidGeometry("Invalid LineString Geometry: should contain at least two coordinates")
        }

        return super.toJson()
    }

}
