@file:JvmName("App")

package io.cyberbarrista.wallet.rest

import Player
import io.cyberbarrista.wallet.rest.Property.APP_PORT
import io.cyberbarrista.wallet.rest.Property.DB_FILE
import io.cyberbarrista.wallet.rest.Property.DB_POOL_SIZE
import io.cyberbarrista.wallet.rest.feature.DatabaseFeature
import io.cyberbarrista.wallet.rest.feature.FlywayFeature
import io.cyberbarrista.wallet.rest.PlayerDto.Currency.USD
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun main() {
    embeddedServer(factory = CIO, port = APP_PORT.getFromEnv().toInt()) {
        val dataSource = install(DatabaseFeature) {
            storageFile = DB_FILE.getFromEnv()
            topPoolSize = DB_POOL_SIZE.getFromEnv().toInt()
        }

        install(FlywayFeature) {
            this.dataSource = dataSource
        }

        runBlocking {
            newSuspendedTransaction {
                Player.insert {
                    it[id] = 0
                    it[balance] = 1.0
                    it[currency] = USD.name
                }
            }
        }

        routing {
            get("/info") {
                val inserted = newSuspendedTransaction { Player.selectAll().first().toPlayerDto() }
                call.respondText("Inserted row: $inserted")
            }
        }
    }.start(wait = true)
}
