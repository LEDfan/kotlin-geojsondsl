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

import com.beust.klaxon.*
import java.util.*

class FeatureCollection {

    private var features = ArrayList<Feature>()

    fun toJson(): JsonObject {
        return json {
            val args: MutableList<Pair<String, Any?>> = mutableListOf("type" to "FeatureCollection")
            args.add("features" to features.map { it.toJson() })
            obj(args)
        }
    }

    fun withFeature(feature: Feature.() -> Unit) {
        features.add(Feature().apply(feature))
    }

    fun withFeature(feature: Feature) {
        features.add(feature)
    }

}

fun featureCollection(featureCollection: FeatureCollection.() -> Unit = {}): FeatureCollection {
    with(FeatureCollection()) {
        this.apply(featureCollection)
        return this
    }
}
