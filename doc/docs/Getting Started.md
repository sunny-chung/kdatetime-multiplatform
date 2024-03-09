# Getting Started

![Maven Central](https://img.shields.io/maven-central/v/io.github.sunny-chung/kdatetime-multiplatform)

## For Single Platform

In build.gradle.kts, add the dependency.
```kotlin
    dependencies {
        implementation("io.github.sunny-chung:kdatetime-multiplatform:<version>")
        // ...
    }
```

## For Kotlin Multiplatform

In build.gradle.kts, add the dependency.
```kotlin
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.github.sunny-chung:kdatetime-multiplatform:<version>")
                // ...
            }
        }
        // ...
```

### Use in Swift / Objective-C

Add a transitive export to the `framework` DSL in build.gradle.kts:
```kotlin
        framework {
            baseName = "shared"
            transitiveExport = true
            export("io.github.sunny-chung:kdatetime-multiplatform:<version>")
        }
```

In native side, import your common framework to use.
```swift title="Swift"
import shared
```

Note the framework base name `shared` is customizable.
