package com.leysoft.infrastructure.json


import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import kotlin.reflect.KClass

object Json {

    private val mapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(Jdk8Module())
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    @Throws(RuntimeException::class)
    fun <A: Any> write(data: A): String =
        when (val result = Try { mapper.writeValueAsString(data) }) {
            is Success -> result.value
            is Failure -> throw RuntimeException(result.exception)
        }

    @Throws(RuntimeException::class)
    fun <A: Any> writeMap(data: A): Map<*, *> =
        when (val result = Try { mapper.convertValue(data, Map::class.java) }) {
            is Success -> result.value
            is Failure -> throw RuntimeException(result.exception)
        }

    @Throws(RuntimeException::class)
    fun <A: Any> read(data: String, clazz: KClass<A>) : A =
        when (val result = Try { mapper.convertValue(data, clazz.java) }) {
            is Success -> result.value
            is Failure -> throw RuntimeException(result.exception)
        }
}