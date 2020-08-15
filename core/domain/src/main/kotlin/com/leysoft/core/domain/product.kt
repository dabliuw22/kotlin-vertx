package com.leysoft.core.domain

import java.time.OffsetDateTime
import java.util.UUID

data class ProductId(val value: String)

data class ProductName(val value: String)

data class ProductStock(val value: Double)

data class ProductCreatedAt(val value: OffsetDateTime)

data class Product(
    val id: ProductId = ProductId(UUID.randomUUID().toString()),
    val name: ProductName,
    val stock: ProductStock,
    val createdAt: ProductCreatedAt = ProductCreatedAt(OffsetDateTime.now())
)
