package com.leysoft.products.adapter.`in`.api

import arrow.core.flatMap
import com.leysoft.core.domain.toProductId
import com.leysoft.infrastructure.http.*
import com.leysoft.products.application.ProductService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.products(service: ProductService) {
    routing {
        route("/products") {
            all(service)
            get(service)
            create(service)
            delete(service)
        }
    }
}

private fun Route.all(service: ProductService) {
    get {
        service.getAll()
            .map { it.map { product -> product.toDto() } }
            .handle({
                call.respondJson(HttpStatusCode.OK, it)
            }) { errorHandler(call) }
    }
}

private fun Route.get(service: ProductService) {
    get("/{id}") {
        call.getRequiredParam("id") { it }
            .map { it.toProductId() }
            .flatMap { service.getBy(it) }
            .handle({
                call.respondJson(HttpStatusCode.OK, it)
            }) { errorHandler(call) }
    }
}

private fun Route.create(service: ProductService) {
    post {
        val product = call.receive<PutProductDto>().toDomain()
        service.create(product)
            .handle({
                call.response.status(HttpStatusCode.Created)
            }) { errorHandler(call) }
    }
}

private fun Route.delete(service: ProductService) {
    delete("/{id}") {
        call.getRequiredParam("id") { it }
            .map { it.toProductId() }
            .flatMap { service.deleteBy(it) }
            .handle({
                call.response.status(HttpStatusCode.Accepted)
            }) { errorHandler(call) }
    }
}
