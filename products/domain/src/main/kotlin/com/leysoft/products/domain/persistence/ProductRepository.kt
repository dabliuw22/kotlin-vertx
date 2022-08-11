package com.leysoft.products.domain.persistence

import arrow.core.Either
import com.leysoft.core.error.ProductException
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId

interface ProductRepository {

    suspend fun findBy(id: ProductId): Either<ProductException, Product>

    suspend fun findAll(): Either<ProductException, List<Product>>

    suspend fun save(product: Product): Either<ProductException, Unit>

    suspend fun deleteBy(id: ProductId): Either<ProductException, Unit>
}
