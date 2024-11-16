import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "1.9.25"

    // using a compatible version with IntelliJ IDEA Android plugin, so that the "androidMain" sourceset can be recognized
    id("com.android.library") version "8.0.2"

    kotlin("plugin.serialization") version "1.9.25"
    kotlin("plugin.parcelize") version "1.9.25"
    id("maven-publish")
    id("sunnychung.publication")
}

group = "io.github.sunny-chung"
version = "1.0.0"

val isGitHubActionsCICD = project.hasProperty("CICD") && project.property("CICD") == "GitHubActions"
if (isGitHubActionsCICD) {
    println("Running with GitHub Actions CI/CD")
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm {
        jvmToolchain(17)
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
    val darwinTargets = listOf<KotlinNativeTarget>(
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
        Note: Code compiled by IR has a running time slower than Legacy for 3X that could not pass the tests.
     */
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            testTask {
                useMocha {
                    timeout = if (isGitHubActionsCICD) {
                        "61s" // GitHub Actions Mac runners are significantly slower
                    } else {
                        "21s"
                    }
                }
            }
        }
        nodejs {
            testTask {
                useMocha {
                    timeout = if (isGitHubActionsCICD) {
                        "61s" // GitHub Actions Mac runners are significantly slower
                    } else {
                        "21s"
                    }
                }
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" -> null // macosX64("native")
//        hostOs == "Linux" -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> {
//            println("Warning: Host OS is not supported in Kotlin/Native.")
//            null
//        }
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
//        if (nativeTarget != null) {
//            val nativeMain by getting
//            val nativeTest by getting
//        }

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

tasks.withType<Test> {
    testLogging {
        events = setOf(TestLogEvent.STARTED, TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
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
