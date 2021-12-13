package io.cyberbarrista.wallet.rest

import io.ktor.application.ApplicationCall
import io.ktor.features.StatusPages
import io.ktor.http.Parameters
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.KClass

enum class Property {
    APP_PORT,
    DB_POOL_SIZE,
    DB_FILE,
}

fun Property.getFromEnv(): String = System.getenv(name)

fun <T : Throwable> StatusPages.Configuration.onExceptions(
    exceptions: List<KClass<out T>>,
    handler: suspend PipelineContext<Unit, ApplicationCall>.(T) -> Unit,
) = exceptions.forEach { exception(it.java, handler) }

fun Parameters.take(name: String): String = this[name] ?: error("$name is not found in $this")
