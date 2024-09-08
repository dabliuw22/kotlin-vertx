package com.leysoft.infrastructure.exposed

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import com.leysoft.core.concurrent.IO
import com.leysoft.infrastructure.exposed.config.ExposedConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object ExposedResource {

    context(ExposedConfig)
    operator fun invoke(): Resource<Exposed> =
        resource {
            val config = this@ExposedConfig
            val database = config.database().bind()
            with(database) {
                with(IO) {
                    Exposed.invoke()
                }
            }
        }

    private fun ExposedConfig.hikari(): Resource<HikariConfig> =
        resource {
            val hikari = HikariConfig()
            hikari.jdbcUrl = url
            hikari.username = user
            hikari.password = password
            hikari.driverClassName = driver
            hikari.maximumPoolSize = poolMaxSize
            hikari.isReadOnly = readOnly
            hikari.transactionIsolation = isolation
            hikari
        }

    private fun ExposedConfig.source(): Resource<HikariDataSource> =
        resource {
            val source = hikari().bind()
            resource { HikariDataSource(source) } release { it.close() }
        }

    private fun ExposedConfig.database(): Resource<Database> =
        resource {
            val source = source().bind()
            Database.connect(datasource = source)
        }
}
