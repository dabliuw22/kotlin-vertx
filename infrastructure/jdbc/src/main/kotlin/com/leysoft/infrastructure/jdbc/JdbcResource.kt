package com.leysoft.infrastructure.jdbc

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import com.leysoft.core.concurrent.IO
import com.leysoft.infrastructure.jdbc.config.JdbcConfig
import com.vladsch.kotlin.jdbc.Session
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.vladsch.kotlin.jdbc.session as jdbc

object JdbcResource {

    context(JdbcConfig)
    operator fun invoke(): Resource<Jdbc> =
        resource {
            val jdbc = this@JdbcConfig
            val session = jdbc.session().bind()
            with(session) {
                with(IO) {
                    Jdbc.invoke()
                }
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
        resource {
            val source = hikari().bind()
            resource { HikariDataSource(source) } release { it.close() }
        }

    private fun JdbcConfig.session(): Resource<Session> =
        resource {
            val source = source().bind()
            resource { jdbc(source) } release { it.close() }
        }
}
