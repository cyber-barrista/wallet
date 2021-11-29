import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

allprojects {
    group = "io.cyberbarrista"
    version = "1.0"

    plugins.apply("org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "17"
    }
}
