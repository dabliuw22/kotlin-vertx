package com.leysoft.infrastructure.http

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.resource
import com.leysoft.infrastructure.http.config.HttpServerConfig
import io.ktor.server.application.*
import io.ktor.server.netty.*

object ServerResource {
    context(HttpServerConfig)
    operator fun invoke(block: Application.() -> Unit): Resource<NettyApplicationEngine> =
        resource {
            Server.create(this@HttpServerConfig, block)
        }
}
