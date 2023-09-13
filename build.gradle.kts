plugins {
    kotlin("multiplatform") version "1.8.0"
}

group = "com.sunnychung.multiplatform"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(8)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
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
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            }
        }
        val jvmMain by getting
        val jvmTest by getting
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
