package com.leysoft.infrastructure.jdbc

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import arrow.fx.coroutines.release
import com.leysoft.core.concurrent.IO
import com.leysoft.infrastructure.jdbc.config.JdbcConfig
import com.vladsch.kotlin.jdbc.Session
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.vladsch.kotlin.jdbc.session as jdbc

object JdbcResource {

    context(JdbcConfig)
    fun make(): Resource<Jdbc> =
        resource {
            val jdbc = this@JdbcConfig
            val session = jdbc.session().bind()
            with(session) {
                with(IO) { Jdbc.make() }
            }
        }

    private fun JdbcConfig.hikari(): Resource<HikariConfig> {
        val config = HikariConfig()
        config.jdbcUrl = url
        config.username = user
        config.password = password
        config.driverClassName = driver
        config.maximumPoolSize = poolMaxSize
        return Resource.just(config)
    }

    private fun JdbcConfig.source(): Resource<HikariDataSource> =
        hikari().flatMap {
            resource { HikariDataSource(it) } release { it.close() }
        }

    private fun JdbcConfig.session(): Resource<Session> =
        source().flatMap {
            resource { jdbc(it) } release { it.close() }
        }
}
