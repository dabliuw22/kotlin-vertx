package com.leysoft.infrastructure.http

import io.ktor.server.application.Application

interface HttpRoute {
    fun Application.route()
}
