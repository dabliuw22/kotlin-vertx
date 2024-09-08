package com.leysoft.infrastructure.json

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

object Json {
    val mapper: ObjectMapper =
        ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .registerModule(Jdk8Module())
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .enable(SerializationFeature.INDENT_OUTPUT)

    fun <A : Any> write(data: A): Either<JsonException, String> = catch { mapper.writeValueAsString(data) }.mapLeft({ JsonException(it) })

    inline fun <reified A : Any> read(data: String): Either<JsonException, A> =
        catch { mapper.readValue(data, A::class.java) }.mapLeft { JsonException(it) }

    data class JsonException(
        val throwable: Throwable,
    ) : RuntimeException(throwable)
}
