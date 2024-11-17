/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    // Apply the common convention plugin for shared build configuration between library and application projects.
    id("jtodo.kotlin-common-conventions")

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    mavenCentral()
}

dependencies {
    constraints {
        implementation("io.javalin:javalin:6.3.0")
    }
}