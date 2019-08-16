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

class LineString: MultiPoint() {

    override val type = "LineString"

    override fun toJson(): JsonObject {
        if (coordinates.size < 2) {
            throw InvalidGeometry("Invalid LineString Geometry: should contain at least two coordinates")
        }

        return super.toJson()
    }

}
