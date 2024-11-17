/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    // Apply the common convention plugin for shared build configuration between library and application projects.
    id("jtodo.kotlin-common-conventions")

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    constraints {
        implementation("org.jdbi:jdbi:2.78")
        implementation("org.postgresql:postgresql:42.7.4")
    }
}