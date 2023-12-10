package com.leysoft.products.adapter.out.persistence.memory

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
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
        either<Throwable, Map<String, Product>> { storage.get() }
            .mapLeft { NotFoundProductException("Not found product: $id") }
            .map { it[id.value] }
            .flatMap { it?.right() ?: NotFoundProductException("Not found product: $id").left() }

    override suspend fun findAll(): Either<ProductException, List<Product>> =
        either<Throwable, List<Product>> {
            storage.get().values.toList()
        }.mapLeft { NotFoundProductException("Not found products") }

    override suspend fun save(product: Product): Either<ProductException, Unit> {
        return when (val result = findBy(product.id)) {
            is Either.Left ->
                when (val error = result.value) {
                    is NotFoundProductException -> either<Throwable, Unit> {
                        storage.update { it.plus(Pair(product.id.value, product)) }
                    }.mapLeft { CreateProductException("Not save Product: ${product.id}") }
                    else -> error.left()
                }
            else -> CreateProductException("Not save Product: ${product.id}").left()
        }
    }

    override suspend fun deleteBy(id: ProductId): Either<ProductException, Unit> {
        return when (val result = findBy(id)) {
            is Either.Right -> {
                return Either.catch({ DeleteProductException("Not delete Product: $id") }) {
                    storage.update { it.minus(id.value) }
                }
            }
            is Either.Left -> result.value.left()
        }
    }

    companion object {
        fun make(storage: Storage): ProductRepository =
            InMemoryProductRepository(storage)
    }
}
