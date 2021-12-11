plugins {
    application
    kotlin("jvm")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "io.cyberbarrista.wallet.rest.App"
    }
}

dependencies {
    implementation(project(":db"))

    implementation("io.ktor:ktor-server-cio:1.6.7")

    implementation("org.jetbrains.exposed:exposed-core:0.35.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.35.1")
    implementation("com.h2database:h2:1.4.200")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("org.flywaydb:flyway-core:8.2.1")

    implementation("ch.qos.logback:logback-classic:1.2.7")
}

tasks.getByPath("build").dependsOn(":db:generateExposedCode")
