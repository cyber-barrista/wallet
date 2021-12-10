import com.jetbrains.exposed.gradle.plugin.DEFAULT_OUTPUT_DIRECTORY
import com.jetbrains.exposed.gradle.plugin.ExposedGenerateCodeTask
import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayMigrateTask

plugins {
    application
    kotlin("jvm")
    id("org.flywaydb.flyway") version "8.2.0"
    id("com.jetbrains.exposed.gradle.plugin") version "0.2.1"
}

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.35.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.35.1")
}

ktlint.filter {
    exclude("**/GeneratedTables.kt/**")
}

val generateDbApiCode = tasks.register("generateDbApiCode") {
    dependsOn(tasks.withType<FlywayCleanTask>())
    dependsOn(tasks.withType<FlywayMigrateTask>())
    finalizedBy(tasks.withType<ExposedGenerateCodeTask>())
}

// TODO The following doesn't work by some reason. Implemented in a more oldschool style for now
// tasks.withType<GradleBuild> {
//    finalizedBy(generateDbApiCode)
// }
tasks.getByPath("build").finalizedBy(generateDbApiCode)

val flywayDb = "jdbc:h2:file:./db"

flyway { url = flywayDb }

exposedCodeGeneratorConfig {
    connectionURL = flywayDb

    sourceSets.main {
        java {
            setSrcDirs(srcDirs + buildDir.resolve(DEFAULT_OUTPUT_DIRECTORY))
        }
    }
}
