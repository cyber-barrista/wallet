package io.cyberbarrista.wallet.rest.dao

import PaymentTransaction
import Player
import io.cyberbarrista.wallet.rest.model.PaymentTransactionDto
import io.cyberbarrista.wallet.rest.model.PaymentTransactionDto.Status.FAILED
import io.cyberbarrista.wallet.rest.model.PaymentTransactionDto.Status.SUCCESSFUL
import io.cyberbarrista.wallet.rest.model.PlayerDto
import io.cyberbarrista.wallet.rest.model.toPaymentTransactionDto
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

object PaymentTransactionDao {
    suspend fun fetchTransactionsBy(playerId: Long): List<PaymentTransactionDto> = newSuspendedTransaction {
        PaymentTransaction
            .select { PaymentTransaction.playerId eq playerId }
            .map { it.toPaymentTransactionDto() }
    }

    suspend fun persistTransaction(transaction: PaymentTransactionDto, updatedPlayer: PlayerDto): Unit =
        newSuspendedTransaction {
            PaymentTransaction.insert {
                it[id] = EntityID(transaction.uuid, PaymentTransaction)
                it[paymentValue] = transaction.paymentValue
                it[playerId] = transaction.playerId
                it[status] = transaction.status.name
            }

            when (transaction.status) {
                SUCCESSFUL -> Player.update({ Player.id eq updatedPlayer.id }) { it[balance] = updatedPlayer.balance }
                FAILED -> {
                }
            }
        }
}
