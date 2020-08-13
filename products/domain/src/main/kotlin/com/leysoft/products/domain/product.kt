package com.leysoft.products.domain

import java.time.OffsetDateTime
import java.util.UUID

data class ProductId(val value: String = UUID.randomUUID().toString())

typealias ProductIdCore = com.leysoft.core.domain.ProductId

fun ProductId.toCore(): ProductIdCore = ProductIdCore(value)

fun ProductIdCore.fromCore(): ProductId = ProductId(value)

data class ProductName(val value: String)

typealias ProductNameCore = com.leysoft.core.domain.ProductName

fun ProductName.toCore(): ProductNameCore = ProductNameCore(value)

fun ProductNameCore.fromCore(): ProductName = ProductName(value)

data class ProductStock(val value: Double)

typealias ProductStockCore = com.leysoft.core.domain.ProductStock

fun ProductStock.toCore(): ProductStockCore = ProductStockCore(value)

fun ProductStockCore.fromCore(): ProductStock = ProductStock(value)

data class ProductCreatedAt(val value: OffsetDateTime = OffsetDateTime.now())

typealias ProductCreatedAtCore = com.leysoft.core.domain.ProductCreatedAt

fun ProductCreatedAt.toCore(): ProductCreatedAtCore = ProductCreatedAtCore(value)

fun ProductCreatedAtCore.fromCore(): ProductCreatedAt = ProductCreatedAt(value)

data class Product(
    val id: ProductId = ProductId(),
    val name: ProductName,
    val stock: ProductStock,
    val createdAt: ProductCreatedAt = ProductCreatedAt()
)

typealias ProductCore = com.leysoft.core.domain.Product

fun Product.toCore(): ProductCore = ProductCore(
    id = id.toCore(),
    name = name.toCore(),
    stock = stock.toCore(),
    createdAt = createdAt.toCore()
)

fun ProductCore.fromCore(): Product = Product(
    id = id.fromCore(),
    name = name.fromCore(),
    stock = stock.fromCore(),
    createdAt = createdAt.fromCore()
)
