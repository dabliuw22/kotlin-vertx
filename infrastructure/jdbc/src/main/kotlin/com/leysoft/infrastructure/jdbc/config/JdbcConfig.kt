package com.leysoft.infrastructure.jdbc.config

import arrow.Kind
import arrow.fx.Resource
import arrow.fx.typeclasses.Bracket
import com.vladsch.kotlin.jdbc.Session
import com.vladsch.kotlin.jdbc.session
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

data class JdbcConfig(
    val url: String,
    val user: String,
    val password: String,
    val driver: String,
    val poolMaxSize: Int
) {

    private val dataSource = dataSource(this)

    fun <F> resource(B: Bracket<F, Throwable>): Resource<F, Throwable, Session> =
        Resource(
            acquire = acquire(B),
            release = release(B),
            BR = B
        )

    private fun <F> acquire(B: Bracket<F, Throwable>): () -> Kind<F, Session> =
        { B.just(session(dataSource)) }

    private fun <F> release(B: Bracket<F, Throwable>): (Session) -> Kind<F, Unit> =
        { B.just(it.close()) }

    companion object {

        private fun hikari(jdbcConfig: JdbcConfig): HikariConfig {
            val config = HikariConfig()
            config.jdbcUrl = jdbcConfig.url
            config.username = jdbcConfig.user
            config.password = jdbcConfig.password
            config.driverClassName = jdbcConfig.driver
            config.maximumPoolSize = jdbcConfig.poolMaxSize
            return config
        }

        private fun dataSource(jdbcConfig: JdbcConfig): DataSource =
            HikariDataSource(hikari(jdbcConfig))
    }
}
