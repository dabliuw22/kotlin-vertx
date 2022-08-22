package com.leysoft.infrastructure.http.config

import arrow.fx.coroutines.Resource
import com.leysoft.core.domain.FromEnv

data class HttpServerConfig(
    val host: String,
    val port: Int
) {
    companion object {
        fun env(): FromEnv<HttpServerConfig> = object : FromEnv<HttpServerConfig> {
            override fun load(): Resource<HttpServerConfig> =
                Resource.just(
                    HttpServerConfig(
                        System.getenv("HTTP_SERVER_HOST") ?: "localhost",
                        System.getenv("HTTP_SERVER_PORT")?.toInt() ?: 8080
                    )
                )
        }
    }
}
