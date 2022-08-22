package com.leysoft.products.adapter.out.persistence.memory

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import arrow.fx.coroutines.Atomic
import com.leysoft.core.error.CreateProductException
import com.leysoft.core.error.DeleteProductException
import com.leysoft.core.error.NotFoundProductException
import com.leysoft.core.error.ProductException
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.persistence.ProductRepository

typealias Storage = Atomic<Map<String, Product>>

class InMemoryProductRepository private constructor(private val storage: Storage) :
    ProductRepository {

    override suspend fun findBy(id: ProductId): Either<ProductException, Product> =
        Either.catch({
            NotFoundProductException("Not found product: $id")
        }) { storage.get() }
            .map { it[id.value] }
            .flatMap { it?.right() ?: Either.Left(NotFoundProductException("Not found product: $id")) }

    override suspend fun findAll(): Either<ProductException, List<Product>> =
        Either.catch({ NotFoundProductException("Not found products") }) {
            storage.get().values.toList()
        }

    override suspend fun save(product: Product): Either<ProductException, Unit> {
        return when (val result = findBy(product.id)) {
            is Either.Left ->
                when (val error = result.value) {
                    is NotFoundProductException -> Either.catch({
                        CreateProductException("Not save Product: ${product.id}")
                    }) {
                        storage.update {
                            it.plus(Pair(product.id.value, product))
                        }
                    }
                    else -> Either.Left(error)
                }
            else -> Either.Left(
                CreateProductException("Not save Product: ${product.id}")
            )
        }
    }

    override suspend fun deleteBy(id: ProductId): Either<ProductException, Unit> {
        return when (val result = findBy(id)) {
            is Either.Right -> {
                return Either.catch({ DeleteProductException("Not delete Product: $id") }) {
                    storage.update { it.minus(id.value) }
                }
            }
            is Either.Left -> Either.Left(result.value)
        }
    }

    companion object {
        fun make(storage: Storage): ProductRepository =
            InMemoryProductRepository(storage)
    }
}
