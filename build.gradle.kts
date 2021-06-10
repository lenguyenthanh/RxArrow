import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val rxJava = "2.2.10"

    const val spek = "2.0.7"
    const val strikt = "0.22.2"
}


plugins {
    kotlin("jvm") version "1.5.10"
    id("java-library")
}

group = "com.lenguyenthanh"
version = "0.10.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

//additional source sets
sourceSets {
    val examples by creating {
        java {
            compileClasspath += sourceSets.main.get().output
            runtimeClasspath += sourceSets.main.get().output
        }
    }
}

//examples configuration
val examplesImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(platform("io.arrow-kt:arrow-stack:0.13.2"))
    implementation("io.arrow-kt:arrow-core")

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:${Versions.rxJava}")

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:${Versions.spek}")

//    testImplementation("io.strikt:strikt-core:${Versions.strikt}")
//    testImplementation("io.strikt:strikt-arrow:${Versions.strikt}")

    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines = setOf("spek2")
    }
}
