package com.leysoft.products.application

import arrow.core.Either
import com.leysoft.core.domain.Product
import com.leysoft.core.domain.ProductId
import com.leysoft.core.error.ProductException
import com.leysoft.products.domain.fromCore
import com.leysoft.products.domain.persistence.ProductRepository
import com.leysoft.products.domain.toCore

interface ProductService {
    suspend fun getBy(id: ProductId): Either<ProductException, Product>

    suspend fun getAll(): Either<ProductException, List<Product>>

    suspend fun create(product: Product): Either<ProductException, Unit>

    suspend fun deleteBy(id: ProductId): Either<ProductException, Unit>

    companion object Instance {
        fun make(repository: ProductRepository): ProductService = object : ProductService {
            override suspend fun getBy(id: ProductId): Either<ProductException, Product> =
                repository.findBy(id.fromCore())
                    .map { it.toCore() }

            override suspend fun getAll(): Either<ProductException, List<Product>> =
                repository.findAll()
                    .map { it.map { product -> product.toCore() } }

            override suspend fun create(product: Product): Either<ProductException, Unit> =
                repository.save(product.fromCore())

            override suspend fun deleteBy(id: ProductId): Either<ProductException, Unit> =
                repository.deleteBy(id.fromCore())
        }
    }
}
