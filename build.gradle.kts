import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val arrow = "0.10.5"
    const val kotlin = "1.3.41"
    const val rxJava = "2.2.10"

    const val spek = "2.0.6"
    const val strikt = "0.22.2"
}


buildscript {
    repositories {
        jcenter()
    }
}

plugins {
    id("java-library")
    kotlin("jvm") version "1.3.41"
    id("maven")
}

group = "com.lenguyenthanh"
version = "0.10.0-SNAPSHOT"

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
    jcenter()
    maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Arrow
    implementation("io.arrow-kt:arrow-core:${Versions.arrow}")
    implementation("io.arrow-kt:arrow-syntax:${Versions.arrow}")

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:${Versions.rxJava}")

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:${Versions.spek}")

    testImplementation("io.strikt:strikt-core:${Versions.strikt}")
    testImplementation("io.strikt:strikt-arrow:${Versions.strikt}")

    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
