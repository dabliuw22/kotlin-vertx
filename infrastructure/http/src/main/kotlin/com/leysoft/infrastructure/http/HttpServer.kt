package com.leysoft.infrastructure.http

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

abstract class HttpServer: AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        routes(router)
        vertx.createHttpServer(options())
            .requestHandler(router)
            .listen(port(), host()) {
                if (it.succeeded()) startFuture?.complete()
                else startFuture?.fail(it.cause())
            }
    }

    private fun options(): HttpServerOptions =
        HttpServerOptions()
            .setCompressionSupported(true)
            .setLogActivity(true)

    abstract fun port(): Int

    abstract fun host(): String

    abstract fun routes(router: Router)
}