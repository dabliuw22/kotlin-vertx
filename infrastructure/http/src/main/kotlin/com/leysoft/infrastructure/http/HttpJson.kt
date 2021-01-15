package com.leysoft.infrastructure.http

import arrow.core.Either
import com.leysoft.infrastructure.json.Json
import kotlin.reflect.KClass

open class HttpJson {

    fun <A : Any> encode(data: A): String =
        when (val json = Json.write(data)) {
            is Either.Right -> json.b
            is Either.Left -> throw json.a
        }

    fun <A : Any> decode(data: String, clazz: KClass<A>): A =
        when (val result = Json.read(data, clazz)) {
            is Either.Right -> result.b
            is Either.Left -> throw result.a
        }

    companion object {
        const val ApplicationJson = "application/json; charset=utf-8"
    }
}
