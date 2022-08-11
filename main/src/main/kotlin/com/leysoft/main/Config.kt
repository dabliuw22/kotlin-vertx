package com.leysoft.main

import arrow.fx.coroutines.Resource
import com.leysoft.core.domain.FromEnv
import com.leysoft.infrastructure.http.config.HttpServerConfig
import com.leysoft.infrastructure.jdbc.config.JdbcConfig

data class Config(
    val server: HttpServerConfig,
    val jdbc: JdbcConfig
) {
    companion object {
        fun env(): FromEnv<Config> = object : FromEnv<Config> {
            override fun load(): Resource<Config> =
                HttpServerConfig.env().load()
                    .flatMap { http ->
                        JdbcConfig.env().load()
                            .map { sql -> Config(http, sql) }
                    }
        }
    }
}
