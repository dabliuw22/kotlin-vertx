package com.leysoft.infrastructure.logger

import arrow.fx.typeclasses.Effect

object LoggerFactory {

    fun <F> getLogger(Q: Effect<F>, clazz: String): Logger<F> =
        DefaultLogger.make(Q, clazz)
}
