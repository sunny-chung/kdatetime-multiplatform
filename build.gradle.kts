plugins {
    kotlin("multiplatform") version "1.8.21"

    // using a compatible version with IntelliJ IDEA Android plugin, so that the "androidMain" sourceset can be recognized
    id("com.android.library") version "8.0.2"

    kotlin("plugin.serialization") version "1.8.21"
    kotlin("plugin.parcelize") version "1.8.21"
    id("maven-publish")
}

group = "com.sunnychung.multiplatform"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm {
        jvmToolchain(8)
//        withJava() // not compatiable with Android Gradle plugin
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
        publishAllLibraryVariants()
    }
    val darwinTargets = listOf(
        iosArm64(),
        iosSimulatorArm64(),
        iosX64()
    )
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.1") // 1.6.0
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
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
        val androidTest by creating {
            dependsOn(commonJvmTest)
            dependsOn(androidMain)
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
        val jsMain by getting
        val jsTest by getting
//        val nativeMain by getting
//        val nativeTest by getting

        configure(darwinTargets) {
            compilations["main"].defaultSourceSet.dependsOn(darwinMain)
            compilations["test"].defaultSourceSet.dependsOn(darwinTest)
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
