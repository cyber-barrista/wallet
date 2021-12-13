package io.cyberbarrista.wallet.rest.dao

import Player
import io.cyberbarrista.wallet.rest.model.PlayerDto
import io.cyberbarrista.wallet.rest.model.toPlayerDto
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object PlayerDao {
    suspend fun fetchPlayerById(id: Long): PlayerDto? = newSuspendedTransaction {
        Player
            .select { Player.id eq id }
            .firstOrNull()
            ?.toPlayerDto()
    }
}
