package com.leysoft.products.adapter.out.persistence.memory

import arrow.core.None
import arrow.core.Some
import arrow.fx.IO
import arrow.fx.Ref
import arrow.fx.extensions.io.effect.effect
import arrow.fx.extensions.io.monadDefer.monadDefer
import arrow.fx.fix
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.ProductName
import com.leysoft.products.domain.ProductStock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object InMemoryProductRepositorySpec : Spek({

    val product = Product(
        name = ProductName("Test Name"),
        stock = ProductStock(100.0)
    )

    val store = Ref(IO.monadDefer(), mapOf(product.id.value to product))
        .fix().unsafeRunSync()

    val repository = InMemoryProductRepository.make(IO.effect(), store)

    describe("InMemoryProductRepository.findBy") {
        it("Return Some") {
            val result = repository.findBy(product.id).fix().unsafeRunSync()
            val status = when (result) {
                is Some -> result.t == product
                else -> false
            }
            assert(status)
        }
    }

    describe("InMemoryProductRepository.findBy") {
        it("Return None") {
            val result = repository.findBy(ProductId()).fix().unsafeRunSync()
            val status = when (result) {
                is None -> true
                else -> false
            }
            assert(status)
        }
    }

    describe("InMemoryProductRepository.findAll") {
        it("Return One") {
            val result = repository.findAll().fix().unsafeRunSync()
            assert(result == listOf(product))
        }
    }
})
