@file:JvmName("App")

package io.cyberbarrista.wallet.rest

import Player
import io.cyberbarrista.wallet.rest.Property.APP_PORT
import io.cyberbarrista.wallet.rest.Property.DB_FILE
import io.cyberbarrista.wallet.rest.Property.DB_POOL_SIZE
import io.cyberbarrista.wallet.rest.feature.DatabaseFeature
import io.cyberbarrista.wallet.rest.feature.FlywayFeature
import io.cyberbarrista.wallet.rest.service.PlayerService
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

fun main() {
    embeddedServer(factory = CIO, port = APP_PORT.getFromEnv().toInt()) {
        val dataSource = install(DatabaseFeature) {
            storageFile = DB_FILE.getFromEnv()
            topPoolSize = DB_POOL_SIZE.getFromEnv().toInt()
        }

        install(FlywayFeature) {
            this.dataSource = dataSource
        }

        install(StatusPages) {
            onExceptions(
                listOf(
                    IllegalStateException::class,
                    NumberFormatException::class,
                    IllegalArgumentException::class,
                    ExposedSQLException::class,
                ),
            ) { call.respond(HttpStatusCode.BadRequest, it.toString()) }
        }

        populateFakeUsers()

        routing {
            route("/players") {
                // Usage example: curl -X GET 'localhost:8080/players/1/balance'
                get("{id}/balance") {
                    call.respondText(
                        "Balance: ${PlayerService.playerBalance(call.parameters.take("id").toLong())}"
                    )
                }

                // Usage example: curl -X POST 'localhost:8080/players/1/debit?transaction-uuid=0468ba84-5bb0-11ec-bf63-0242ac130002&debit-amount=1.0'
                post("{id}/debit") {
                    PlayerService.debitPlayer(
                        playerId = call.parameters.take("id").toLong(),
                        transactionUuid = UUID.fromString(call.request.queryParameters.take("transaction-uuid")),
                        debitAmount = call.request.queryParameters.take("debit-amount").toDouble()
                    )
                }

                // Usage example: curl -X POST 'localhost:8080/players/1/credit?transaction-uuid=0468ba84-5bb0-11ec-bf63-0242ac130002&credit-amount=1.0'
                post("{id}/credit") {
                    PlayerService.creditPlayer(
                        playerId = call.parameters.take("id").toLong(),
                        transactionUuid = UUID.fromString(call.request.queryParameters.take("transaction-uuid")),
                        creditAmount = call.request.queryParameters.take("credit-amount").toDouble()
                    )
                }

                // Usage example: curl -X GET 'localhost:8080/players/1/transaction-history'
                get("{id}/transaction-history") {
                    call.respondText(PlayerService.getTransactionHistory(call.parameters.take("id").toLong()))
                }
            }
        }
    }.start(wait = true)
}

// TODO (to delete) populate some fake players to play around
private fun populateFakeUsers() = transaction {
    if (Player.selectAll().empty()) {
        Player.batchInsert(listOf(5.0, 10.0, 15.0, 20.0)) { this[Player.balance] = it }
    }
}
