package com.leysoft.infrastructure.http

import arrow.core.Either
import com.leysoft.infrastructure.json.Json

open class HttpJson {
    fun <A : Any> encode(data: A): String =
        when (val json = Json.write(data)) {
            is Either.Right -> json.value
            is Either.Left -> throw json.value
        }

    inline fun <reified A : Any> decode(data: String): A =
        when (val result = Json.read<A>(data)) {
            is Either.Right -> result.value
            is Either.Left -> throw result.value
        }

    companion object {
        const val APPLICATION_JSON = "application/json; charset=utf-8"
    }
}
