package com.leysoft.infrastructure.logger

import org.slf4j.LoggerFactory

interface Logger {
    fun info(message: String)

    fun error(message: String)

    fun error(
        message: String,
        throwable: Throwable,
    )

    fun debug(message: String)

    fun warn(message: String)

    fun trace(message: String)

    companion object {
        inline fun <reified A> get(): Logger =
            object : Logger {
                private val logger = LoggerFactory.getLogger(A::class.java)

                override fun info(message: String) = logger.info(message)

                override fun error(message: String) = logger.error(message)

                override fun error(
                    message: String,
                    throwable: Throwable,
                ) = logger.error(message, throwable)

                override fun debug(message: String) = logger.debug(message)

                override fun warn(message: String) = logger.warn(message)

                override fun trace(message: String) = logger.trace(message)
            }
    }
}
