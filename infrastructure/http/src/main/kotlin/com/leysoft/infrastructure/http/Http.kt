package com.leysoft.infrastructure.http

import com.leysoft.infrastructure.json.Json
import io.vertx.core.json.JsonObject
import kotlin.reflect.KClass

object Http {

    fun <A: Any> encode(data: A): String = JsonObject(
        Json.writeMap(data) as Map<String, Any>
    ).encode()

    fun <A: Any> decode(data: JsonObject, clazz: KClass<A>): A =
        Json.read(data.encode(), clazz)
}