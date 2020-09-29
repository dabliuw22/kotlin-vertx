package com.leysoft.infrastructure.logger

import arrow.Kind
import arrow.fx.typeclasses.Effect
import org.slf4j.LoggerFactory

interface Logger<F> {

    fun info(message: String): Kind<F, Unit>

    fun error(message: String): Kind<F, Unit>

    fun error(message: String, throwable: Throwable): Kind<F, Unit>

    fun debug(message: String): Kind<F, Unit>

    fun warn(message: String): Kind<F, Unit>

    fun trace(message: String): Kind<F, Unit>
}

class DefaultLogger<F> private constructor (
    private val Q: Effect<F>,
    private val clazz: String
) : Logger<F> {

    private val logger = LoggerFactory.getLogger(clazz)

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

    companion object {

        fun <F> make(Q: Effect<F>, clazz: String) : Logger<F> =
            DefaultLogger(Q, clazz)
    }
}
