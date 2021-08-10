plugins {
    id("idea")
    id("maven-publish")
    kotlin("jvm") version "1.5.21"
}

group = "io.github.bridgelol"
version = "1.0"

repositories {
    mavenCentral()
}

tasks {
    build {
        dependsOn(publishToMavenLocal)
    }
}