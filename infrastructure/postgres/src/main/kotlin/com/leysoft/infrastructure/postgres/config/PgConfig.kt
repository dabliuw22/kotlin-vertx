package com.leysoft.infrastructure.postgres.config

import arrow.Kind
import arrow.fx.Resource
import arrow.fx.typeclasses.Bracket
import com.vladsch.kotlin.jdbc.Session
import com.vladsch.kotlin.jdbc.session
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

data class PgConfig(
    val url: String,
    val user: String,
    val password: String,
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

        private fun hikari(pgConfig: PgConfig): HikariConfig {
            val config = HikariConfig()
            config.jdbcUrl = pgConfig.url
            config.username = pgConfig.user
            config.password = pgConfig.password
            config.driverClassName = "org.postgresql.Driver"
            config.maximumPoolSize = pgConfig.poolMaxSize
            return config
        }

        private fun dataSource(pgConfig: PgConfig): DataSource =
            HikariDataSource(hikari(pgConfig))
    }
}
