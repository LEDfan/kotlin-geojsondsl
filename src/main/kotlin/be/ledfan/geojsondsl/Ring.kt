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

import com.beust.klaxon.JsonArray
import com.beust.klaxon.json

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