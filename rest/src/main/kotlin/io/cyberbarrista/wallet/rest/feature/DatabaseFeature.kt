package io.cyberbarrista.wallet.rest.feature

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource
import kotlin.properties.Delegates

object DatabaseFeature : ApplicationFeature<ApplicationCallPipeline, DatabaseFeature.Config, DataSource> {
    class Config {
        lateinit var storageFile: String
        var topPoolSize by Delegates.notNull<Int>()
    }

    override val key = AttributeKey<DataSource>("${DataSource::class}")

    override fun install(pipeline: ApplicationCallPipeline, configure: Config.() -> Unit): HikariDataSource {
        val hikariConfig = with(Config().apply(configure)) {
            HikariConfig().apply {
                driverClassName = org.h2.Driver::class.qualifiedName
                jdbcUrl = "jdbc:h2:file:$storageFile"
                maximumPoolSize = topPoolSize
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            }
        }

        return HikariDataSource(hikariConfig.also { it.validate() }).also { Database.connect(it) }
    }
}
