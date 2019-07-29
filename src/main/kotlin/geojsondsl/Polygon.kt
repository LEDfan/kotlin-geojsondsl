package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json

class Polygon : Geometry {

    override val coordinates = ArrayList<Ring>()

    override val type = "Polygon"

    fun ring(ring: Ring.() -> Unit = {}) {
        val r = Ring()
        r.apply(ring)
        r.ensureValid()
        coordinates.add(r)
    }

    override fun toJson(): JsonObject {
        //  "For Polygons with more than one of these rings, the first MUST be
        //  the exterior ring, and any others MUST be interior rings.  The
        //  exterior ring bounds the surface, and the interior rings (if
        //  present) bound holes within the surface."
        //  is not checked. (TODO)

        return json {
            obj(
                    "type" to type,
                    "coordinates" to array(
                            coordinates.map { it.toJson() }
                    )
            )
        }
    }

}
