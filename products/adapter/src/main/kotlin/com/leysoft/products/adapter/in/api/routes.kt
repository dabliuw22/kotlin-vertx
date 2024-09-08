package com.leysoft.products.adapter.`in`.api

import arrow.core.raise.fold
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
        fold(
            block = {
                service.getAll().map { product -> product.toDto() }
            },
            recover = { errorHandler(it).let { response -> call.respondJson(response.code, response) } },
            catch = { throwableHandler(it).let { response -> call.respondJson(response.code, response) } },
            transform = { call.respondJson(HttpStatusCode.OK, it) },
        )
    }
}

private fun Route.get(service: ProductService) {
    get("/{id}") {
        fold(
            block = {
                val id = call.getRequiredParam("id") { it.toProductId() }
                service.getBy(id).toDto()
            },
            recover = { errorHandler(it).let { response -> call.respondJson(response.code, response) } },
            catch = { throwableHandler(it).let { response -> call.respondJson(response.code, response) } },
            transform = { call.respondJson(HttpStatusCode.OK, it) },
        )
    }
}

private fun Route.create(service: ProductService) {
    post {
        fold(
            block = {
                val product = call.receive<PutProductDto>().toDomain()
                service.create(product).toDto()
            },
            recover = { errorHandler(it).let { response -> call.respondJson(response.code, response) } },
            catch = { throwableHandler(it).let { response -> call.respondJson(response.code, response) } },
            transform = { call.respondJson(HttpStatusCode.Created, it) },
        )
    }
}

private fun Route.delete(service: ProductService) {
    delete("/{id}") {
        fold(
            block = {
                val id = call.getRequiredParam("id") { it.toProductId() }
                service.deleteBy(id)
            },
            recover = { errorHandler(it).let { response -> call.respondJson(response.code, response) } },
            catch = { throwableHandler(it).let { response -> call.respondJson(response.code, response) } },
            transform = { call.respondJson(HttpStatusCode.OK, it) },
        )
    }
}
