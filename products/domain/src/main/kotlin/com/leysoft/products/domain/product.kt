package com.leysoft.products.domain

import java.time.OffsetDateTime
import java.util.UUID

data class ProductId(val value: String = UUID.randomUUID().toString())

data class ProductName(val value: String)

data class ProductStock(val value: Double)

data class ProductCreatedAt(val value: OffsetDateTime = OffsetDateTime.now())

data class Product(
    val id: ProductId = ProductId(),
    val name: ProductName,
    val stock: ProductStock,
    val createdAt: ProductCreatedAt = ProductCreatedAt()
)
