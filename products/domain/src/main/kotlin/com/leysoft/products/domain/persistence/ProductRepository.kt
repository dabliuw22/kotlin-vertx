package com.leysoft.products.domain.persistence

import arrow.Kind
import arrow.core.Option
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId

interface ProductRepository<F> {

    fun findBy(id: ProductId): Kind<F, Option<Product>>

    fun findAll(): Kind<F, List<Product>>

    fun save(product: Product): Kind<F, Unit>

    fun deleteBy(id: ProductId): Kind<F, Boolean>
}