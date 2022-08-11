package com.leysoft.infrastructure.jdbc

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.release
import arrow.fx.coroutines.resource
import com.leysoft.infrastructure.jdbc.config.SqlConfig
import com.vladsch.kotlin.jdbc.Session
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.vladsch.kotlin.jdbc.session as jdbc

object SqlResource {

    context(SqlConfig)
    fun make(): Resource<Sql> =
        Resource.just(this@SqlConfig)
            .flatMap { it.session() }
            .map { with(it) { Sql.make() } }

    private fun SqlConfig.hikari(): Resource<HikariConfig> {
        val config = HikariConfig()
        config.jdbcUrl = url
        config.username = user
        config.password = password
        config.driverClassName = driver
        config.maximumPoolSize = poolMaxSize
        return Resource.just(config)
    }

    private fun SqlConfig.source(): Resource<HikariDataSource> =
        hikari().flatMap {
            resource { HikariDataSource(it) } release { it.close() }
        }

    private fun SqlConfig.session(): Resource<Session> =
        source().flatMap {
            resource { jdbc(it) } release { it.close() }
        }
}
