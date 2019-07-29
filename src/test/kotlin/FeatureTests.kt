import be.ledfan.geojsondsl.*
import com.beust.klaxon.JsonArray
import com.beust.klaxon.json
import geojsondsl.ICoordinate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

data class Coordinate(val lat: Double, val lon: Double) : ICoordinate {
    override fun toJson(): JsonArray<Double> {
        return json {
            array(
                    lon,
                    lat
            )
        } as JsonArray<Double>
    }
}

data class ThreeDCoordinate(val lat: Double, val lon: Double, val alt: Double) : ICoordinate {
    override fun toJson(): JsonArray<Double> {
        return json {
            array(
                    lon,
                    lat,
                    alt
            )
        } as JsonArray<Double>
    }
}

class FeatureTests {

    @Test
    fun normal_linestring() {
        val actual: String = feature {
            withGeometry {
                lineString {
                    withCoordinate(Coordinate(10.0, 10.0))
                    withCoordinate(Coordinate(10.0, 10.0))
                }
            }
        }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"LineString","coordinates":[[10.0,10.0],[10.0,10.0]]},"properties":{}}""", actual)
    }

    @Test
    fun error_linestring_one_coordinate() {
        val actual = feature {
            withGeometry {
                lineString {
                    withCoordinate(Coordinate(10.0, 10.0))
                }
            }
        }

        val exception = assertThrows(InvalidGeometry::class.java) { actual.toJson() }
        assertEquals("Invalid LineString Geometry: should contain at least two coordinates", exception.message)
    }

    @Test
    fun normal_point() {
        val actual: String = feature {
            withGeometry {
                point(Coordinate(10.0, 10.0))
            }
        }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"Point","coordinates":[10.0,10.0]},"properties":{}}""", actual)
    }

    @Test
    fun normal_multipoint() {
        val actual: String = feature {
            withGeometry {
                multiPoint {
                    // only one coordinate is allowed
                    withCoordinate(Coordinate(10.0, 10.0))
                }
            }
        }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"MultiPoint","coordinates":[[10.0,10.0]]},"properties":{}}""", actual)
    }

    @Test
    fun error_with_geometry() {
        val exception = assertThrows(InvalidGeometry::class.java) {
            feature {
                withGeometry {
                    point(Coordinate(10.0, 10.0))
                }
                withGeometry {
                    point(Coordinate(10.0, 10.0))
                }
            }
        }
        assertEquals("Feature already contains a geometry or geometrycollection", exception.message)
    }

    @Test
    fun error_multi_geometry() {
        var exception = assertThrows(InvalidGeometry::class.java) {
            feature {
                withGeometry {
                    point(Coordinate(10.0, 10.0))
                    point(Coordinate(10.0, 10.0))
                }
            }
        }
        assertEquals("WithGeometry already contains a geometry", exception.message)
        exception = assertThrows(InvalidGeometry::class.java) {
            feature {
                withGeometry {
                    point(Coordinate(10.0, 10.0))
                    multiPoint {
                        withCoordinate(Coordinate(10.0, 10.0))
                    }
                }
            }
        }
        assertEquals("WithGeometry already contains a geometry", exception.message)

        exception = assertThrows(InvalidGeometry::class.java) {
            feature {
                withGeometry {
                    point(Coordinate(10.0, 10.0))
                    lineString {
                        withCoordinate(Coordinate(10.0, 10.0))
                    }
                }
            }
        }
        assertEquals("WithGeometry already contains a geometry", exception.message)
    }

    @Test
    fun error_with_property() {
        val exception = assertThrows(Exception::class.java) {
            feature {
                withGeometry {
                    point(Coordinate(10.0, 10.0))
                }
                withProperty("test", "1")
                withProperty("test", "2")
            }
        }
        assertEquals("""Feature already has a property with key "test"""", exception.message)
    }

    @Test
    fun error_with_complex_property() {
        val exception = assertThrows(Exception::class.java) {
            feature {
                withGeometry {
                    point(Coordinate(10.0, 10.0))
                }
                withProperty("test") { array(1) }
                withProperty("test") { array(1) }
            }
        }
        assertEquals("""Feature already has a property with key "test"""", exception.message)
    }

    @Test
    fun normal_with_properties() {
        val actual: String =
                feature {
                    withGeometry {
                        point(Coordinate(10.0, 10.0))
                    }
                    withProperty("test1", "1") // simple property
                    withProperty("test2", "2") // simple property
                    withProperty("test3") {
                        // complex json array property
                        array(1, 2, 3)
                    }
                    withProperty("test4") {
                        // complex json object property
                        obj("a" to "b", "b" to array(1, 2))
                    }
                }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"Point","coordinates":[10.0,10.0]},"properties":{"test4":{"a":"b","b":[1,2]},"test2":"2","test3":[1,2,3],"test1":"1"}}""", actual)
    }

    @Test
    fun normal_multi_linestring() {
        val actual = feature {
            withGeometry {
                multiLineString {
                    lineString {
                        withCoordinate(Coordinate(1.1, 2.2))
                        withCoordinate(Coordinate(3.3, 4.4))
                    }
                    lineString {
                        withCoordinate(Coordinate(5.5, 6.6))
                        withCoordinate(Coordinate(7.7, 8.8))
                    }
                }
            }
        }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"MultiLineString","coordinates":[[[2.2,1.1],[4.4,3.3]],[[6.6,5.5],[8.8,7.7]]]},"properties":{}}""", actual)
    }

    @Test
    fun normal_polygon() {
        val actual: String = feature {
            withGeometry {
                polygon {
                    ring {
                        withCoordinate(Coordinate(1.1, 2.2))
                        withCoordinate(Coordinate(3.3, 4.4))
                        withCoordinate(Coordinate(3.3, 4.6))
                        withCoordinate(Coordinate(1.1, 2.2))
                    }
                    ring {
                        withCoordinate(Coordinate(0.5, 3.14))
                        withCoordinate(Coordinate(5.6, 4.2))
                        withCoordinate(Coordinate(8.8, 7.6))
                        withCoordinate(Coordinate(4.2, 5.2))
                        withCoordinate(Coordinate(0.5, 3.14))
                    }
                }
            }
        }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[2.2,1.1],[4.4,3.3],[4.6,3.3],[2.2,1.1]],[[3.14,0.5],[4.2,5.6],[7.6,8.8],[5.2,4.2],[3.14,0.5]]]},"properties":{}}""", actual)
    }

    @Test
    fun error_polygon() {
        var exception = assertThrows(InvalidRing::class.java) {
            feature {
                withGeometry {
                    polygon {
                        ring {
                            withCoordinate(Coordinate(1.1, 2.2))
                            withCoordinate(Coordinate(3.3, 4.4))
                            withCoordinate(Coordinate(3.3, 4.6))
                        }
                    }
                }
            }
        }

        assertEquals("Invalid Ring: should contain at least four coordinates, contains: [Coordinate(lat=1.1, lon=2.2), Coordinate(lat=3.3, lon=4.4), Coordinate(lat=3.3, lon=4.6)]", exception.message)

        exception = assertThrows(InvalidRing::class.java) {
            feature {
                withGeometry {
                    polygon {
                        ring {
                            withCoordinate(Coordinate(1.1, 2.2))
                            withCoordinate(Coordinate(3.3, 4.4))
                            withCoordinate(Coordinate(3.3, 4.4))
                            withCoordinate(Coordinate(3.3, 4.6))
                        }
                    }
                }
            }
        }

        assertEquals("Invalid Ring: first and last coordinate should be equal, first is : Coordinate(lat=1.1, lon=2.2), last is: Coordinate(lat=3.3, lon=4.6)", exception.message)
    }

    @Test
    fun normal_multi_poylgon() {
        val actual: String = feature {
            withGeometry {
                multiPolygon {
                    polygon {
                        ring {
                            withCoordinate(Coordinate(1.1, 2.2))
                            withCoordinate(Coordinate(3.3, 4.4))
                            withCoordinate(Coordinate(3.3, 4.6))
                            withCoordinate(Coordinate(1.1, 2.2))
                        }
                        ring {
                            withCoordinate(Coordinate(0.5, 3.14))
                            withCoordinate(Coordinate(5.6, 4.2))
                            withCoordinate(Coordinate(8.8, 7.6))
                            withCoordinate(Coordinate(4.2, 5.2))
                            withCoordinate(Coordinate(0.5, 3.14))
                        }
                    }
                    polygon {
                        ring {
                            withCoordinate(Coordinate(1.1, 2.2))
                            withCoordinate(Coordinate(3.3, 4.4))
                            withCoordinate(Coordinate(3.3, 4.6))
                            withCoordinate(Coordinate(1.1, 2.2))
                        }
                    }
                }
            }
        }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"MultiPolygon","coordinates":[[[[2.2,1.1],[4.4,3.3],[4.6,3.3],[2.2,1.1]],[[3.14,0.5],[4.2,5.6],[7.6,8.8],[5.2,4.2],[3.14,0.5]]],[[[2.2,1.1],[4.4,3.3],[4.6,3.3],[2.2,1.1]]]]},"properties":{}}""", actual)
    }

    @Test
    fun error_multi_polygon() {
        var exception = assertThrows(InvalidRing::class.java) {
            feature {
                withGeometry {
                    multiPolygon {
                        polygon {
                            ring {
                                withCoordinate(Coordinate(1.1, 2.2))
                                withCoordinate(Coordinate(3.3, 4.4))
                                withCoordinate(Coordinate(3.3, 4.6))
                            }
                        }
                    }
                }
            }
        }

        assertEquals("Invalid Ring: should contain at least four coordinates, contains: [Coordinate(lat=1.1, lon=2.2), Coordinate(lat=3.3, lon=4.4), Coordinate(lat=3.3, lon=4.6)]", exception.message)

        exception = assertThrows(InvalidRing::class.java) {
            feature {
                withGeometry {
                    multiPolygon {
                        polygon {
                            ring {
                                withCoordinate(Coordinate(1.1, 2.2))
                                withCoordinate(Coordinate(3.3, 4.4))
                                withCoordinate(Coordinate(3.3, 4.4))
                                withCoordinate(Coordinate(3.3, 4.6))
                            }
                        }
                    }
                }
            }
        }

        assertEquals("Invalid Ring: first and last coordinate should be equal, first is : Coordinate(lat=1.1, lon=2.2), last is: Coordinate(lat=3.3, lon=4.6)", exception.message)
    }

    @Test
    fun normal_geometry_collection() {
        val actual: String = feature {
            withGeometryCollection {
                point(Coordinate(1.0, 2.0))
                lineString {
                    withCoordinate(Coordinate(1.0, 2.0))
                    withCoordinate(Coordinate(3.0, 4.0))
                }
            }
        }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"GeometryCollection","geometries":[{"type":"Point","coordinates":[2.0,1.0]},{"type":"LineString","coordinates":[[2.0,1.0],[4.0,3.0]]}]},"properties":{}}""", actual)
    }


    @Test
    fun error_geometry_collection() {
        val exception = assertThrows(Exception::class.java) {
            feature {
                withGeometryCollection {
                    point(Coordinate(1.0, 2.0))
                    lineString {
                        withCoordinate(Coordinate(1.0, 2.0))
                        withCoordinate(Coordinate(3.0, 4.0))
                    }
                }
                withGeometryCollection {
                    point(Coordinate(1.0, 2.0))
                }
            }.toJson().toJsonString()
        }

        assertEquals("""Feature already contains a geometry or geometrycollection""", exception.message)
    }


    @Test
    fun null_geometry() {
        val actual = feature { }.toJson().toJsonString()
        assertEquals("""{"type":"Feature","geometry":null,"properties":{}}""", actual)
    }

    @Test
    fun normal_id() {
        var actual = feature { withId(1000L) }.toJson().toJsonString()
        assertEquals("""{"type":"Feature","id":1000,"geometry":null,"properties":{}}""", actual)
        actual = feature { withId("test-id") }.toJson().toJsonString()
        assertEquals("""{"type":"Feature","id":"test-id","geometry":null,"properties":{}}""", actual)
    }

    @Test
    fun normal_feature_collection() {
        val actual = featureCollection {
            withFeature {
                withId("test")
                withGeometry {
                    point(Coordinate(1.0, 1.0))
                }
            }
            withFeature {
                withId("test")
                withGeometryCollection {
                    multiPoint {
                        withCoordinate(Coordinate(2.0, 3.0))
                        withCoordinate(Coordinate(4.0, 5.0))
                    }
                    point(Coordinate(5.0, 6.0))
                }
            }
        }.toJson().toJsonString()
        assertEquals("""{"type":"FeatureCollection","features":[{"type":"Feature","id":"test","geometry":{"type":"Point","coordinates":[1.0,1.0]},"properties":{}},{"type":"Feature","id":"test","geometry":{"type":"GeometryCollection","geometries":[{"type":"MultiPoint","coordinates":[[3.0,2.0],[5.0,4.0]]},{"type":"Point","coordinates":[6.0,5.0]}]},"properties":{}}]}""", actual)
    }

    @Test
    fun threeD_test() {
        val actual = feature {
            withGeometry {
                point(ThreeDCoordinate(1.0, 2.0, 3.0))
            }
        }.toJson().toJsonString()

        assertEquals("""{"type":"Feature","geometry":{"type":"Point","coordinates":[2.0,1.0,3.0]},"properties":{}}""", actual)
    }
}