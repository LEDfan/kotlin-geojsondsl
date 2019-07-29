package be.ledfan.geojsondsl

class GeometryFactory {

    var geometry: Geometry? = null

    fun multiPoint(multiPoint: MultiPoint.() -> Unit = {}): MultiPoint = create(multiPoint)

    fun lineString(lineString: LineString.() -> Unit = {}): LineString = create(lineString)

    fun multiLineString(multiLineString: MultiLineString.() -> Unit = {}): MultiLineString = create(multiLineString)

    fun polygon(polygon: Polygon.() -> Unit = {}): Polygon = create(polygon)

    fun multiPolygon(multiPolygon: MultiPolygon.() -> Unit = {}): MultiPolygon = create(multiPolygon)

    fun point(coordinate: ICoordinate): Point {
        if (geometry != null) {
            throw InvalidGeometry("WithGeometry already contains a geometry")
        }

        val r = Point(coordinate)
        geometry = r
        return r
    }

    private inline fun <reified T : Geometry> create(func: T.() -> Unit = {}): T {
        if (geometry != null) {
            throw InvalidGeometry("WithGeometry already contains a geometry")
        }

        val r = T::class.java.newInstance()
        r.apply(func)
        geometry = r
        return r
    }
}
