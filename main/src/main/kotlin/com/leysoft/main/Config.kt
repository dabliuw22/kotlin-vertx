package com.leysoft.main

import arrow.fx.coroutines.Resource
import com.leysoft.core.domain.FromEnv
import com.leysoft.infrastructure.http.config.HttpServerConfig
import com.leysoft.infrastructure.jdbc.config.SqlConfig

data class Config(
    val server: HttpServerConfig,
    val sql: SqlConfig
) {
    companion object {
        fun env(): FromEnv<Config> = object : FromEnv<Config> {
            override fun load(): Resource<Config> =
                HttpServerConfig.env().load()
                    .flatMap { http ->
                        SqlConfig.env().load()
                            .map { sql -> Config(http, sql) }
                    }
        }
    }
}
