package com.leysoft.products.domain.persistence

import arrow.core.raise.Raise
import com.leysoft.core.error.ProductException
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId

interface ProductRepository {
    context(Raise<ProductException>)
    suspend fun findBy(id: ProductId): Product

    context(Raise<ProductException>)
    suspend fun findAll(): List<Product>

    context(Raise<ProductException>)
    suspend fun save(product: Product): Product

    context(Raise<ProductException>)
    suspend fun deleteBy(id: ProductId)
}
