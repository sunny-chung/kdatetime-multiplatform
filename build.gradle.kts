import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("multiplatform") version "1.8.21"

    // using a compatible version with IntelliJ IDEA Android plugin, so that the "androidMain" sourceset can be recognized
    id("com.android.library") version "8.0.2"

    kotlin("plugin.serialization") version "1.8.21"
    kotlin("plugin.parcelize") version "1.8.21"
    id("maven-publish")
    id("sunnychung.publication")
}

group = "io.github.sunny-chung"
version = "0.5.0"

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm {
        jvmToolchain(8)
//        withJava() // not compatible with Android Gradle plugin
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
//        publishAllLibraryVariants()
        publishLibraryVariants = listOf("release")
    }
    val darwinTargets = listOf(
        iosArm64(),
        iosSimulatorArm64(),
        iosX64(),
        watchosArm64(),
        watchosSimulatorArm64(),
        watchosX64(),
        tvosArm64(),
        tvosSimulatorArm64(),
        tvosX64(),
        macosArm64(),
        macosX64()
    )
    /*
        Not using IR because its running time is slower than Legacy for 3X that could not pass the tests.
        Will use IR when Kotlin is upgraded to 1.9 which using IR is forced and may fix the issue.
     */
    js(LEGACY) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            testTask {
                useMocha {
                    timeout = "11s"
                }
            }
        }
        nodejs {
            testTask {
                useMocha {
                    timeout = "11s"
                }
            }
        }
    }
//    val hostOs = System.getProperty("os.name")
//    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" -> macosX64("native")
//        hostOs == "Linux" -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }

    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        val commonJvmMain by creating {
            dependsOn(commonMain)
        }
        val commonJvmTest by creating {
            dependsOn(commonMain)
        }
        val androidMain by getting {
            dependsOn(commonJvmMain)
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.8.21")
            }
        }
        val nonAndroidJvmMain by creating {
            dependsOn(commonJvmMain)
        }
        val nonAndroidJvmTest by creating {
            dependsOn(commonJvmTest)
            dependsOn(nonAndroidJvmMain)
        }
        val jvmMain by getting {
            dependsOn(nonAndroidJvmMain)
        }
        val jvmTest by getting {
            dependsOn(nonAndroidJvmTest)
        }
        val darwinMain by creating {
            dependsOn(commonMain)
        }
        val darwinTest by creating {
            dependsOn(commonTest)
        }
        val iosMain by creating {
            dependsOn(darwinMain)
        }
        val watchosMain by creating {
            dependsOn(darwinMain)
        }
        val tvosMain by creating {
            dependsOn(darwinMain)
        }
        val macosMain by creating {
            dependsOn(darwinMain)
        }
        val jsMain by getting
        val jsTest by getting
//        val nativeMain by getting
//        val nativeTest by getting

        configure(darwinTargets) {
            val (mainSourceSet, testSourceSet) = when {
                name.startsWith("ios") -> Pair(iosMain, darwinTest)
                name.startsWith("watchos") -> Pair(watchosMain, darwinTest)
                name.startsWith("tvos") -> Pair(tvosMain, darwinTest)
                name.startsWith("macos") -> Pair(macosMain, darwinTest)
                else -> throw UnsupportedOperationException("Target $name is not supported")
            }
            compilations["main"].defaultSourceSet.dependsOn(mainSourceSet)
            compilations["test"].defaultSourceSet.dependsOn(testSourceSet)
        }
    }
}

android {
    namespace = "com.sunnychung.lib.android.kdatetime"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = artifactGroup
//            artifactId = "kdatetime-multiplatform"
//            version = artifactVersion
//
////            afterEvaluate {
////                from(components["release"])
////            }
//        }
//    }
//}
