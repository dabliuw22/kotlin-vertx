package com.leysoft.core.domain

import java.time.ZonedDateTime
import java.util.UUID

@JvmInline
value class ProductId(val value: String)
fun String.toProductId(): ProductId = ProductId(this)

@JvmInline
value class ProductName(val value: String)
fun String.toProductName(): ProductName = ProductName(this)

@JvmInline
value class ProductStock(val value: Double)
fun Double.toProductStock(): ProductStock = ProductStock(this)

@JvmInline
value class ProductCreatedAt(val value: ZonedDateTime)
fun ZonedDateTime.toProductCreatedAt(): ProductCreatedAt =
    ProductCreatedAt(this)

data class Product(
    val id: ProductId = ProductId(UUID.randomUUID().toString()),
    val name: ProductName,
    val stock: ProductStock,
    val createdAt: ProductCreatedAt = ProductCreatedAt(ZonedDateTime.now())
)
