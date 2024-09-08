package com.leysoft.main

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.continuations.resource
import com.leysoft.core.domain.FromEnv
import com.leysoft.infrastructure.exposed.config.ExposedConfig
import com.leysoft.infrastructure.http.config.HttpServerConfig
import com.leysoft.infrastructure.jdbc.config.JdbcConfig

data class Config(
    val server: HttpServerConfig,
    val jdbc: JdbcConfig,
    val exposed: ExposedConfig,
) {
    companion object {
        fun env(): FromEnv<Config> =
            object : FromEnv<Config> {
                override fun load(): Resource<Config> =
                    resource {
                        val http = HttpServerConfig.env().load().bind()
                        val sql = JdbcConfig.env().load().bind()
                        val exposed = ExposedConfig.env().load().bind()
                        Config(http, sql, exposed)
                    }
            }
    }
}
