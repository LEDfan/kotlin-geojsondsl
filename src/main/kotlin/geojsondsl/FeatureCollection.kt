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

}

fun featureCollection(featureCollection: FeatureCollection.() -> Unit = {}): FeatureCollection {
    with(FeatureCollection()) {
        this.apply(featureCollection)
        return this
    }
}
