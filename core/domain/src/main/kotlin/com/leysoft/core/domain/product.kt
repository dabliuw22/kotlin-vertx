package com.leysoft.core.domain

import java.time.OffsetDateTime
import java.util.UUID

data class ProductId(val value: String)
fun String.toProductId(): ProductId = ProductId(this)

data class ProductName(val value: String)
fun String.toProductName(): ProductName = ProductName(this)

data class ProductStock(val value: Double)
fun Double.toProductStock(): ProductStock = ProductStock(this)

data class ProductCreatedAt(val value: OffsetDateTime)
fun OffsetDateTime.toProductCreatedAt(): ProductCreatedAt =
    ProductCreatedAt(this)

data class Product(
    val id: ProductId = ProductId(UUID.randomUUID().toString()),
    val name: ProductName,
    val stock: ProductStock,
    val createdAt: ProductCreatedAt = ProductCreatedAt(OffsetDateTime.now())
)
