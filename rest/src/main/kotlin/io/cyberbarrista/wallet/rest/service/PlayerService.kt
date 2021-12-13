package io.cyberbarrista.wallet.rest.service

import io.cyberbarrista.wallet.rest.dao.PaymentTransactionDao
import io.cyberbarrista.wallet.rest.dao.PlayerDao
import io.cyberbarrista.wallet.rest.model.PaymentTransactionDto
import io.cyberbarrista.wallet.rest.model.PaymentTransactionDto.Status.FAILED
import io.cyberbarrista.wallet.rest.model.PaymentTransactionDto.Status.SUCCESSFUL
import io.cyberbarrista.wallet.rest.model.PlayerDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

// TODO (Non existing yet) Unsigned Double would suite better as money amount type
object PlayerService {
    private val LOG: Logger = LoggerFactory.getLogger(this::class.java)

    suspend fun playerBalance(playerId: Long): Double? = PlayerDao
        .fetchPlayerById(playerId)
        ?.balance
        ?: null.also { LOG.warn("No player with id=$playerId") }

    suspend fun debitPlayer(playerId: Long, transactionUuid: UUID, debitAmount: Double) {
        val player = PlayerDao.fetchPlayerById(playerId) ?: error("No player with id=$playerId".error())

        val transaction = PaymentTransactionDto(
            uuid = transactionUuid,
            paymentValue = -debitAmount,
            playerId = playerId,
            status = player.evaluateIndebtingSuccess(debitAmount),
        )

        PaymentTransactionDao.persistTransaction(transaction, player.copy(balance = player.balance - debitAmount))
        LOG.info("Transaction=$transaction is persisted")
    }

    suspend fun creditPlayer(playerId: Long, transactionUuid: UUID, creditAmount: Double) {
        val player = PlayerDao.fetchPlayerById(playerId) ?: error("No player with id=$playerId".error())

        val transaction = PaymentTransactionDto(
            uuid = transactionUuid,
            paymentValue = creditAmount,
            playerId = playerId,
            status = SUCCESSFUL,
        )

        PaymentTransactionDao.persistTransaction(transaction, player.copy(balance = player.balance + creditAmount))
        LOG.info("Transaction=$transaction is persisted")
    }

    suspend fun getTransactionHistory(playerId: Long) = PaymentTransactionDao
        .fetchTransactionsBy(playerId)
        .joinToString()

    private fun PlayerDto.evaluateIndebtingSuccess(debitAmount: Double) = if (balance - debitAmount >= 0) {
        SUCCESSFUL
    } else {
        FAILED
    }

    private fun String.error() = also { LOG.error(it) }
}
