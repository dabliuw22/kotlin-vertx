package com.leysoft.products.adapter.`in`.api

import com.leysoft.products.domain.Product
import java.time.OffsetDateTime

data class ProductDto(
    val id: String,
    val name: String,
    val stock: Double,
    val createdAt: OffsetDateTime
)

fun Product.toDto() : ProductDto = ProductDto(
    id = id.value,
    name = name.value,
    stock = stock.value,
    createdAt = createdAt.value
)