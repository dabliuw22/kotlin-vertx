package com.leysoft.products.adapter.out.persistence

import arrow.Kind
import arrow.core.None
import arrow.core.Option
import arrow.fx.Ref
import arrow.fx.typeclasses.Effect
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.persistence.ProductRepository
import java.lang.RuntimeException

class InMemoryProductRepository<F> private constructor(
    private val Q: Effect<F>,
    private val store: Ref<F, Map<String, Product>>):
    ProductRepository<F>, Effect<F> by Q {

    override fun findBy(id: ProductId): Kind<F, Option<Product>> = store.get()
        .map { Option.fromNullable(it[id.value]) }

    override fun findAll(): Kind<F, List<Product>> = store.get()
        .map { it.values.toList() }

    override fun save(product: Product): Kind<F, Unit> = store.get()
        .map { Option.fromNullable(it[product.id.value]) }
        .flatMap { result ->
            when(result) {
                is None -> store.update { it.plus(Pair(product.id.value, product)) }
                else    -> raiseError(RuntimeException("Not save Product: ${product.id}"))
            }
        }

    override fun deleteBy(id: ProductId): Kind<F, Boolean> = store.get()
        .map { Option.fromNullable(it[id.value]) }
        .flatMap {
            it.fold(
                { raiseError<Boolean>(RuntimeException("Not found Product: $id")) },
                {
                    store.update { s -> s.minus(id.value) }
                        .map { true }.handleError { false }
                }
            )
        }
}