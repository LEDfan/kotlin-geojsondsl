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
