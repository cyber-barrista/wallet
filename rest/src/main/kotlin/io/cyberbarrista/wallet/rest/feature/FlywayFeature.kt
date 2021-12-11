package io.cyberbarrista.wallet.rest.feature

import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import org.flywaydb.core.Flyway
import javax.sql.DataSource

object FlywayFeature : ApplicationFeature<ApplicationCallPipeline, FlywayFeature.Config, Unit> {
    class Config {
        lateinit var dataSource: DataSource
    }

    override val key = AttributeKey<Unit>("")

    override fun install(pipeline: ApplicationCallPipeline, configure: Config.() -> Unit) {
        Flyway
            .configure()
            .dataSource(Config().apply(configure).dataSource)
            .load()
            .migrate()
    }
}
