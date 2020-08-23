package com.leysoft.products.application

import arrow.Kind
import arrow.core.Some
import arrow.fx.typeclasses.Effect
import com.leysoft.core.domain.Product
import com.leysoft.core.domain.ProductId
import com.leysoft.core.error.CreateProductException
import com.leysoft.core.error.DeleteProductException
import com.leysoft.core.error.NotFoundProductException
import com.leysoft.products.domain.fromCore
import com.leysoft.products.domain.persistence.ProductRepository
import com.leysoft.products.domain.toCore
import java.lang.RuntimeException

interface ProductService<F> {

    fun getBy(id: ProductId): Kind<F, Product>

    fun getAll(): Kind<F, List<Product>>

    fun create(product: Product): Kind<F, Unit>

    fun deleteBy(id: ProductId): Kind<F, Unit>
}

class DefaultProductService<F> private constructor(
    private val Q: Effect<F>,
    private val repository: ProductRepository<F>
) : ProductService<F>, Effect<F> by Q {

    override fun getBy(id: ProductId): Kind<F, Product> =
        repository.findBy(id.fromCore())
            .map { it.map { product -> product.toCore() } }
            .map {
                when (it) {
                    is Some -> it.t
                    else -> throw NotFoundProductException("Not Found Product: ${id.value}")
                }
            }

    override fun getAll(): Kind<F, List<Product>> =
        repository.findAll()
            .map { it.map { product -> product.toCore() } }

    override fun create(product: Product): Kind<F, Unit> =
        just(product)
            .map { it.fromCore() }
            .flatMap { repository.save(it) }
            .handleError {
                throw CreateProductException("Error Creating the Product: $product")
            }

    override fun deleteBy(id: ProductId): Kind<F, Unit> =
        repository.deleteBy(id.fromCore())
            .flatMap {
                if (it) just(Unit)
                else raiseError(RuntimeException())
            }.handleError {
                throw DeleteProductException("Error Deleting the Product: ${id.value}")
            }

    companion object {

        fun <F> make(Q: Effect<F>, repository: ProductRepository<F>): ProductService<F> =
            DefaultProductService(Q, repository)
    }
}
