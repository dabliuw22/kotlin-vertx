package com.leysoft.infrastructure.http

import com.leysoft.infrastructure.json.Json
import io.vertx.core.json.JsonObject
import kotlin.reflect.KClass

open class HttpJson {

    fun <A: Any> encode(data: A): String =
        JsonObject(Json.write(data)).encode()

    fun <A: Any> decode(data: JsonObject, clazz: KClass<A>): A =
        Json.read(data.encode(), clazz)

    companion object {
        const val ApplicationJson = "application/json; charset=utf-8"
    }
}