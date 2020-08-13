package com.leysoft.products.adapter.`in`.api

import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductName
import com.leysoft.products.domain.ProductStock
import java.time.OffsetDateTime

data class GetProductDto(
    val id: String,
    val name: String,
    val stock: Double,
    val createdAt: OffsetDateTime
)

fun Product.toDto(): GetProductDto = GetProductDto(
    id = id.value,
    name = name.value,
    stock = stock.value,
    createdAt = createdAt.value
)

data class PutProductDto(
    val name: String,
    val stock: Double
)

fun PutProductDto.toDomain(): Product = Product(
    name = ProductName(name),
    stock = ProductStock(stock)
)
