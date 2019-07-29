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
