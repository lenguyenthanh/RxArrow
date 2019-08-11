import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    val arrow = "0.10.0-SNAPSHOT"
    val kotlin = "1.3.41"
    val rxJava = "2.2.10"
}


buildscript {
    repositories {
        jcenter()
    }
}

plugins {
    id("java-library")
    kotlin("jvm") version "1.3.41"
}

group = "com.lenguyenthanh"
version = "0.10.0-SNAPSHOT"

repositories {
    jcenter()
    maven(url="https://oss.jfrog.org/artifactory/oss-snapshot-local/")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Arrow
    api("io.arrow-kt:arrow-core:${Versions.arrow}")
    api("io.arrow-kt:arrow-syntax:${Versions.arrow}")

    // RxJava
    api("io.reactivex.rxjava2:rxjava:${Versions.rxJava}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}