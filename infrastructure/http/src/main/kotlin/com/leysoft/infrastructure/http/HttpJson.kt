package com.leysoft.infrastructure.http

import com.leysoft.infrastructure.json.Json
import io.vertx.core.json.JsonObject
import kotlin.reflect.KClass

open class HttpJson {

    fun <A : Any> encode(data: A): String =
        Json.write(data)

    fun <A : Any> decode(data: String, clazz: KClass<A>): A =
        Json.read(data, clazz)

    companion object {
        const val ApplicationJson = "application/json; charset=utf-8"
    }
}
