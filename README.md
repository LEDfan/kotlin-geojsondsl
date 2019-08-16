Kotlin GeoJSON DSL
==================

[![Build Status](https://travis-ci.com/LEDfan/kotlin-geojsondsl.svg?token=AhWiySeGEDkQfLDToshu&branch=master)](https://travis-ci.com/LEDfan/kotlin-geojsondsl)

This is a small Kotlin library to easily generate [GeoJSON](https://geojson.org/) using a DSL.
The library uses the [Klaxon](https://github.com/cbeust/klaxon) library.


## Minimal example

For example, the following Kotlin code:
```kotlin
println(feature {
    withGeometry {
        point(Coordinate(4.3516970, 50.8465573))
        withProperty("city", "Brussels")
        withProperty("country", "Belgium")
    }
}.toJson().toJsonString(prettyPrint = true))
```
will print:
```json
{
  "type": "Feature",
  "geometry": {
    "type": "Point",
    "coordinates": [50.8465573, 4.351697]
  },
  "properties": {
    "country": "Belgium",
    "city": "Brussels"
  }
}
```

## Coordinate

The library does not depend on any specific geometry library. In order to represent a coordinate, the library uses the `ICoordinate` interface:
```kotlin
interface ICoordinate {
    fun toJson(): JsonArray<Double>
}
```
Before you can use the library, you have to implement this interface, for example using:
```kotlin
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
```

### Feature
To create a feature object, simply call the `feature` function with a [trailling lambda](https://kotlinlang.org/docs/reference/lambdas.html#passing-a-lambda-to-the-last-parameter). In this lambda the following functions are available:

 - `withGeometry` add a geometry to the feature
 - `withGeometryCollection` add a collection of geometries to the feature
 - `withProperty` add a property to the feature. The value can either be `Any` or a trailling lambda in which you can call the `array` or `obj` function os klaxon.
 - `withId` add a id of type string or number to the feature

[Above](#minimal-example) you already find an example of `withGeometry`.
The following is an example of a GeometryCollection:
```kotlin
println(feature {
    withGeometryCollection {
        point(Coordinate(1.0, 2.0))
        lineString {
            withCoordinate(Coordinate(1.0, 2.0))
            withCoordinate(Coordinate(3.0, 4.0))
        }
    }
}.toJson().toJsonString(prettyPrint = true))
```
which prints:
```json
{
  "type": "Feature",
  "geometry": {
    "type": "GeometryCollection",
    "geometries": [
      {
        "type": "Point",
        "coordinates": [
          2.0,
          1.0
        ]
      },
      {
        "type": "LineString",
        "coordinates": [
          [
            2.0,
            1.0
          ],
          [
            4.0,
            3.0
          ]
        ]
      }
    ]
  },
  "properties": {}
}
```

Examples of `withProperty`:
```kotlin
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
}.toJson().toJsonString(prettyPrint = true))
```
which prints
```json
{
  "type": "Feature",
  "geometry": {
    "type": "Point",
    "coordinates": [
      10.0,
      10.0
    ]
  },
  "properties": {
    "test4": {
      "a": "b",
      "b": [
        1,
        2
      ]
    },
    "test2": "2",
    "test3": [
      1,
      2,
      3
    ],
    "test1": "1"
  }
}
```

### FeatureCollection
A featureCollection can be created similar to a feature by calling the `featureCollection` function. For example:
```kotlin
featureCollection {
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
}.toJson().toJsonString(prettyPrint = true))
```
```json
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "id": "test",
      "geometry": {
        "type": "Point",
        "coordinates": [
          1.0,
          1.0
        ]
      },
      "properties": {}
    },
    {
      "type": "Feature",
      "id": "test",
      "geometry": {
        "type": "GeometryCollection",
        "geometries": [
          {
            "type": "MultiPoint",
            "coordinates": [
              [
                3.0,
                2.0
              ],
              [
                5.0,
                4.0
              ]
            ]
          },
          {
            "type": "Point",
            "coordinates": [
              6.0,
              5.0
            ]
          }
        ]
      },
      "properties": {}
    }
  ]
}
```

### Point
```kotlin
feature {
    withGeometry {
        point(Coordinate(1.0, 1.0))
    }
}
```
```json
{"type":"Feature","geometry":{"type":"Point","coordinates":[10.0,10.0]},"properties":{}}
```

### MultiPoint
```kotlin
feature {
    withGeometry {
        multiPoint {
            // only one coordinate is allowed
            withCoordinate(Coordinate(10.0, 10.0))
            withCoordinate(Coordinate(12.0, 12.0))
        }
    }
}
```
```json
{"type":"Feature","geometry":{"type":"MultiPoint","coordinates":[[10.0,10.0],[12.0,12.0]]},"properties":{}}
```

### LineString
```kotlin
feature {
    withGeometry {
        lineString {
            withCoordinate(Coordinate(10.0, 10.0))
            withCoordinate(Coordinate(10.0, 10.0))
        }
    }
}
```
```json
{"type":"Feature","geometry":{"type":"LineString","coordinates":[[10.0,10.0],[10.0,10.0]]},"properties":{}}
```


### MultiLineString
```kotlin
feature {
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
}
```
```json
{"type":"Feature","geometry":{"type":"MultiLineString","coordinates":[[[2.2,1.1],[4.4,3.3]],[[6.6,5.5],[8.8,7.7]]]},"properties":{}}
```

### Polygon
```kotlin
feature {
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
}
```
```json
{
  "type": "Feature",
  "geometry": {
    "type": "Polygon",
    "coordinates": [
      [
        [
          2.2,
          1.1
        ],
        [
          4.4,
          3.3
        ],
        [
          4.6,
          3.3
        ],
        [
          2.2,
          1.1
        ]
      ],
      [
        [
          3.14,
          0.5
        ],
        [
          4.2,
          5.6
        ],
        [
          7.6,
          8.8
        ],
        [
          5.2,
          4.2
        ],
        [
          3.14,
          0.5
        ]
      ]
    ]
  },
  "properties": {}
}
```

### MultiPolygon
```kotlin
feature {
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
}
```
```json
{
  "type": "Feature",
  "geometry": {
    "type": "MultiPolygon",
    "coordinates": [
      [
        [
          [
            2.2,
            1.1
          ],
          [
            4.4,
            3.3
          ],
          [
            4.6,
            3.3
          ],
          [
            2.2,
            1.1
          ]
        ],
        [
          [
            3.14,
            0.5
          ],
          [
            4.2,
            5.6
          ],
          [
            7.6,
            8.8
          ],
          [
            5.2,
            4.2
          ],
          [
            3.14,
            0.5
          ]
        ]
      ],
      [
        [
          [
            2.2,
            1.1
          ],
          [
            4.4,
            3.3
          ],
          [
            4.6,
            3.3
          ],
          [
            2.2,
            1.1
          ]
        ]
      ]
    ]
  },
  "properties": {}
}
```

## License

>   Copyright 2019 Tobia De Koninck
>
>   Licensed under the Apache License, Version 2.0 (the "License");
>   you may not use this file except in compliance with the License.
>   You may obtain a copy of the License at
>
>       http://www.apache.org/licenses/LICENSE-2.0
>
>   Unless required by applicable law or agreed to in writing, software
>   distributed under the License is distributed on an "AS IS" BASIS,
>   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>   See the License for the specific language governing permissions and
>   limitations under the License.
