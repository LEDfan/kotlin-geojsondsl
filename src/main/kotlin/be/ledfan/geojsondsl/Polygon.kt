/**
 *    Copyright 2019 Tobia De Koninck
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package be.ledfan.geojsondsl

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
import be.ledfan.geojsondsl.Geometry

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
