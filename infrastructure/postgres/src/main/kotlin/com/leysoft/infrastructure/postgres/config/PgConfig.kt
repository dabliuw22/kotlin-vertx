package com.leysoft.infrastructure.postgres.config

import arrow.fx.Resource
import arrow.fx.typeclasses.Bracket
import com.vladsch.kotlin.jdbc.session
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

data class PgConfig(
    val url: String,
    val user: String,
    val password: String,
    val poolMaxSize: Int
) {

    fun <F> session(B: Bracket<F, Throwable>, pgConfig: PgConfig) = Resource(
        { B.just(session(dataSource(pgConfig))) },
        { s -> B.just(s.close()) },
        B
    )

    companion object {

        private fun hikari(pgConfig: PgConfig): HikariConfig {
            val config = HikariConfig()
            config.jdbcUrl = pgConfig.url
            config.username = pgConfig.user
            config.password = pgConfig.password
            config.driverClassName = "org.postgresql.ds.PGSimpleDataSource"
            config.maximumPoolSize = pgConfig.poolMaxSize
            return config
        }

        private fun dataSource(pgConfig: PgConfig) = HikariDataSource(hikari(pgConfig))
    }
}
