package com.leysoft.infrastructure.jdbc.config

import arrow.fx.coroutines.Resource
import com.leysoft.core.domain.FromEnv

data class SqlConfig(
    val url: String,
    val user: String,
    val password: String,
    val driver: String,
    val poolMaxSize: Int
) {
    companion object {
        fun env(): FromEnv<SqlConfig> = object : FromEnv<SqlConfig> {
            override fun load(): Resource<SqlConfig> =
                Resource.just(
                    SqlConfig(
                        System.getenv("DB_URL")
                            ?: "jdbc:postgresql://localhost:5432/vertx_db",
                        System.getenv("DB_USER") ?: "vertx",
                        System.getenv("DB_PASSWORD") ?: "vertx",
                        System.getenv("DB_DRIVER") ?: "org.postgresql.Driver",
                        System.getenv("DB_POOL_MAX_SIZE")?.toInt() ?: 10
                    )
                )
        }
    }
}
