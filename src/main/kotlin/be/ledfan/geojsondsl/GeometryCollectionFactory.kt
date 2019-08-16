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

class GeometryCollectionFactory {

    var geometryCollection = GeometryCollection()

    fun multiPoint(multiPoint: MultiPoint.() -> Unit = {}): MultiPoint = create(multiPoint)

    fun lineString(lineString: LineString.() -> Unit = {}): LineString = create(lineString)

    fun multiLineString(multiLineString: MultiLineString.() -> Unit = {}): MultiLineString = create(multiLineString)

    fun polygon(polygon: Polygon.() -> Unit = {}): Polygon = create(polygon)

    fun multiPolygon(multiPolygon: MultiPolygon.() -> Unit = {}): MultiPolygon = create(multiPolygon)

    fun point(coordinate: ICoordinate): Point {
        val r = Point(coordinate)
        geometryCollection.geometries.add(r)
        return r
    }

    private inline fun <reified T : Geometry> create(func: T.() -> Unit = {}): T {

        val r = T::class.java.newInstance()
        r.apply(func)
        geometryCollection.geometries.add(r)
        return r
    }
}
