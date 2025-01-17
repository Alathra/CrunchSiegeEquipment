pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0" // allow automatic download of JDKs 
}

rootProject.name = "SiegeEngines"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")