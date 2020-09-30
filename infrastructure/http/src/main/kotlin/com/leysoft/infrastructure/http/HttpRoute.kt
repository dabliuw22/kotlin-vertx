package com.leysoft.infrastructure.http

import io.vertx.ext.web.Router

interface HttpRoute {

    fun route(router: Router)
}
