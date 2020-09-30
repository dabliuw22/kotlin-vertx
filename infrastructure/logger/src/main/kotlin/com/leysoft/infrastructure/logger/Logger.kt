package com.leysoft.infrastructure.logger

import arrow.Kind

interface Logger<F> {

    fun info(message: String): Kind<F, Unit>

    fun error(message: String): Kind<F, Unit>

    fun error(message: String, throwable: Throwable): Kind<F, Unit>

    fun debug(message: String): Kind<F, Unit>

    fun warn(message: String): Kind<F, Unit>

    fun trace(message: String): Kind<F, Unit>
}
