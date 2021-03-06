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
