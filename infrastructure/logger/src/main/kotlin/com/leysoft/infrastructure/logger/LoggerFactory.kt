package com.leysoft.infrastructure.logger

import arrow.Kind
import arrow.fx.typeclasses.Effect
import org.slf4j.LoggerFactory

object LoggerFactory {
    inline fun <reified A, F> getLogger(Q: Effect<F>): Logger<F> =
        object : Logger<F> {
            private val logger = LoggerFactory.getLogger(A::class.java)

            override fun info(message: String): Kind<F, Unit> =
                Q.defer { Q.effect { logger.info(message) } }

            override fun error(message: String): Kind<F, Unit> =
                Q.defer { Q.effect { logger.error(message) } }

            override fun error(message: String, throwable: Throwable): Kind<F, Unit> =
                Q.defer { Q.effect { logger.error(message, throwable) } }

            override fun debug(message: String): Kind<F, Unit> =
                Q.defer { Q.effect { logger.debug(message) } }

            override fun warn(message: String): Kind<F, Unit> =
                Q.defer { Q.effect { logger.warn(message) } }

            override fun trace(message: String): Kind<F, Unit> =
                Q.defer { Q.effect { logger.trace(message) } }
        }
}
