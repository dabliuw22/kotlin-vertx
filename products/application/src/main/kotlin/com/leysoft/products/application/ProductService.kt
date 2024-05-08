package com.leysoft.products.application

import arrow.core.raise.Raise
import com.leysoft.core.domain.Product
import com.leysoft.core.domain.ProductId
import com.leysoft.core.error.ProductException
import com.leysoft.products.domain.fromCore
import com.leysoft.products.domain.persistence.ProductRepository
import com.leysoft.products.domain.toCore

interface ProductService {
    context(Raise<ProductException>)
    suspend fun getBy(id: ProductId): Product

    context(Raise<ProductException>)
    suspend fun getAll(): List<Product>

    context(Raise<ProductException>)
    suspend fun create(product: Product)

    context(Raise<ProductException>)
    suspend fun deleteBy(id: ProductId)

    companion object {

        operator fun invoke(repository: ProductRepository): ProductService = object : ProductService {

            context(Raise<ProductException>)
            override suspend fun getBy(id: ProductId): Product {
                val result = repository.findBy(id.fromCore())
                return result.toCore()
            }

            context(Raise<ProductException>)
            override suspend fun getAll(): List<Product> =
                repository.findAll()
                    .map { it.toCore() }

            context(Raise<ProductException>)
            override suspend fun create(product: Product) =
                repository.save(product.fromCore())

            context(Raise<ProductException>)
            override suspend fun deleteBy(id: ProductId) =
                repository.deleteBy(id.fromCore())
        }
    }
}
