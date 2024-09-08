package com.leysoft.products.adapter.`in`.api

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.leysoft.core.domain.Product
import com.leysoft.core.domain.ProductName
import com.leysoft.core.domain.ProductStock
import java.time.ZonedDateTime

data class GetProductDto(
    val id: String,
    val name: String,
    val stock: Double,
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    val createdAt: ZonedDateTime,
)

fun Product.toDto(): GetProductDto =
    GetProductDto(
        id = id.value,
        name = name.value,
        stock = stock.value,
        createdAt = createdAt.value,
    )

data class PutProductDto(
    val name: String,
    val stock: Double,
)

fun PutProductDto.toDomain(): Product =
    Product(
        name = ProductName(name),
        stock = ProductStock(stock),
    )
