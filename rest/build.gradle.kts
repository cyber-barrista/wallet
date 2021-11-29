plugins {
    application
    kotlin("jvm")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "io.cyberbarrista.wallet.rest.App"
    }
}
