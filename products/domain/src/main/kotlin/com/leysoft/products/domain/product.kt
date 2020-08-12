package com.leysoft.products.domain

import java.time.OffsetDateTime

data class ProductId(val value: String)

data class ProductName(val value: String)

data class ProductStock(val value: Double)

data class ProductCreatedAt(val value: OffsetDateTime)

data class Product(
    val id: ProductId,
    val name: ProductName,
    val stock: ProductStock,
    val createdAt: ProductCreatedAt
)