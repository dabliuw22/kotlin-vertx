package com.leysoft.infrastructure.exposed.config

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.resource
import com.leysoft.core.domain.FromEnv

data class ExposedConfig(
    val url: String,
    val user: String,
    val password: String,
    val driver: String,
    val poolMaxSize: Int,
    val readOnly: Boolean,
    val isolation: String,
) {
    companion object {
        fun env(): FromEnv<ExposedConfig> =
            object : FromEnv<ExposedConfig> {
                override fun load(): Resource<ExposedConfig> =
                    resource {
                        ExposedConfig(
                            System.getenv("DB_URL")
                                ?: "jdbc:postgresql://localhost:5432/vertx_db",
                            System.getenv("DB_USER") ?: "vertx",
                            System.getenv("DB_PASSWORD") ?: "vertx",
                            System.getenv("DB_DRIVER") ?: "org.postgresql.Driver",
                            System.getenv("DB_POOL_MAX_SIZE")?.toInt() ?: 10,
                            System.getenv("DB_IS_READ_ONLY")?.toBoolean() ?: false,
                            System.getenv("DB_TRANSACTION_ISOLATION")?.toString() ?: "TRANSACTION_SERIALIZABLE",
                        )
                    }
            }
    }
}
