package com.leysoft.infrastructure.http

import arrow.fx.coroutines.Resource
import com.leysoft.infrastructure.http.config.HttpServerConfig
import io.ktor.server.application.*
import io.ktor.server.netty.*

object ServerResource {
    context(HttpServerConfig)
    fun make(
        block: Application.() -> Unit
    ): Resource<NettyApplicationEngine> =
        Resource.just(
            Server.create(this@HttpServerConfig, block)
        )
}
