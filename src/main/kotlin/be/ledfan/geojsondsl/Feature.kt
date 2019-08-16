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

class Feature {

    private var geometry: Geometry? = null
    private var geometryCollection: GeometryCollection? = null
    private val properties = HashMap<String, Any>()
    private var id: Any? = null // Int or String

    fun toJson(): JsonObject {
        return json {
            val args: MutableList<Pair<String, Any?>> = mutableListOf("type" to "Feature")

            id?.let {
                args.add("id" to it)
            }

            if (geometryCollection != null) {
                args.add("geometry" to geometryCollection?.toJson())
            } else {
                args.add("geometry" to geometry?.toJson())
            }

            args.add("properties" to obj(properties.map { Pair(it.key, it.value) }))

            obj(args)
        }
    }

    fun withGeometry(geometryFactory: GeometryFactory.() -> Unit) {
        if (geometry != null || geometryCollection != null) {
            throw InvalidGeometry("Feature already contains a geometry or geometrycollection")
        }
        geometry = GeometryFactory().apply(geometryFactory).geometry
    }

    fun withGeometryCollection(geometryCollectionFactory: GeometryCollectionFactory.() -> Unit) {
        if (geometry != null || geometryCollection != null) {
            throw InvalidGeometry("Feature already contains a geometry or geometrycollection")
        }
        geometryCollection = GeometryCollectionFactory().apply(geometryCollectionFactory).geometryCollection
    }

    private val theKlaxonJson = object : KlaxonJson {}

    fun <T> withProperty(key: String, json: KlaxonJson.() -> T) {
        if (properties.containsKey(key)) {
            throw Exception("""Feature already has a property with key "$key"""")
        }
        properties[key] = theKlaxonJson.run { json() } as JsonBase
    }

    fun withProperty(key: String, value: Any) {
        if (properties.containsKey(key)) {
            throw Exception("""Feature already has a property with key "$key"""")
        }
        properties[key] = value
    }

    fun withId(id: String) {
        this.id = id
    }

    fun withId(id: Number) {
        this.id = id
    }

}

fun feature(feature: Feature.() -> Unit = {}): Feature {
    with(Feature()) {
        this.apply(feature)
        return this
    }
}
