package io.cyberbarrista.wallet.rest

enum class Property {
    APP_PORT,
    DB_POOL_SIZE,
    DB_FILE,
}

fun Property.getFromEnv(): String = System.getenv(name)
