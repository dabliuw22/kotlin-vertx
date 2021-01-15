package com.leysoft.infrastructure.json

import arrow.core.Either
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.lang.Exception
import kotlin.reflect.KClass

object Json {

    private val mapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(Jdk8Module())
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    fun <A : Any> write(data: A): Either<JsonException, String> =
        try {
            val json = mapper.writeValueAsString(data)
            Either.right(json)
        } catch (e: Exception) {
            Either.left(JsonException(e))
        }

    fun <A : Any> read(data: String, clazz: KClass<A>): Either<JsonException, A> =
        try {
            val result = mapper.readValue(data, clazz.java)
            Either.right(result)
        } catch (e: Exception) {
            Either.left(JsonException(e))
        }

    data class JsonException(val throwable: Throwable) : RuntimeException(throwable)
}
