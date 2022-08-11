package com.leysoft.infrastructure.http

import com.leysoft.infrastructure.http.config.HttpServerConfig
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

object Server {
    fun create(
        config: HttpServerConfig,
        block: Application.() -> Unit
    ) =
        embeddedServer(
            Netty,
            config.port,
            config.host
        ) {
            configure()
            block()
        }

    fun NettyApplicationEngine.run(): NettyApplicationEngine =
        start(true)
}
