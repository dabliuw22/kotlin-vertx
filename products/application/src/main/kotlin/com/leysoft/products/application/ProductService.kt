package com.leysoft.products.application

import arrow.Kind
import arrow.core.Option
import arrow.fx.typeclasses.Effect
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.persistence.ProductRepository

interface ProductService<F> {

    fun findBy(id: ProductId): Kind<F, Option<Product>>

    fun findAll(): Kind<F, List<Product>>

    fun save(product: Product): Kind<F, Unit>

    fun deleteBy(id: ProductId): Kind<F, Boolean>
}

class DefaultProductService<F> private constructor(
    private val Q: Effect<F>,
    private val repository: ProductRepository<F>
) :
    ProductService<F>, Effect<F> by Q {

    override fun findBy(id: ProductId): Kind<F, Option<Product>> =
        repository.findBy(id)

    override fun findAll(): Kind<F, List<Product>> =
        repository.findAll()

    override fun save(product: Product): Kind<F, Unit> =
        repository.save(product)

    override fun deleteBy(id: ProductId): Kind<F, Boolean> =
        repository.deleteBy(id)

    companion object {

        fun <F> make(Q: Effect<F>, repository: ProductRepository<F>): ProductService<F> =
            DefaultProductService(Q, repository)
    }
}
