package io.cyberbarrista.wallet.rest

import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

// TODO Make this code autogenerated too

data class PlayerDto(
    val id: Long,
    val balance: Double,
    val currency: Currency,
) {
    enum class Currency {
        USD,
        EUR,
    }
}

data class PaymentTransactionDto(
    val uuid: UUID,
    val playerId: Long,
    val status: Status,
) {
    enum class Status {
        SUCCESSFUL,
        FAILED,
    }
}

fun ResultRow.toPlayerDto() = PlayerDto(
    id = this[Player.id],
    balance = this[Player.balance],
    currency = enumValueOf(this[Player.currency]),
)

fun ResultRow.toPaymentTransactionDto() = PaymentTransactionDto(
    uuid = this[PaymentTransaction.id].value,
    playerId = this[PaymentTransaction.playerId],
    status = enumValueOf(this[PaymentTransaction.status]),
)