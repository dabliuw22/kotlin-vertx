package com.leysoft.products.application
/*
import arrow.fx.IO
import arrow.fx.Ref
import arrow.fx.extensions.io.effect.effect
import arrow.fx.extensions.io.monad.flatMap
import arrow.fx.extensions.io.monad.map
import arrow.fx.extensions.io.monadDefer.monadDefer
import arrow.fx.fix
import arrow.fx.handleError
import com.leysoft.products.application.Repository.ProductRepositoryTest
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.ProductName
import com.leysoft.products.domain.ProductStock
import com.leysoft.products.domain.fromCore
import com.leysoft.products.domain.toCore
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object DefaultProductServiceSpec : Spek({

    val effect = IO.effect()

    val product = Product(
        name = ProductName("Test Name"),
        stock = ProductStock(100.0)
    )

    val store = Ref(IO.monadDefer(), mapOf(product.id.value to product))
        .fix().unsafeRunSync()

    val repository = ProductRepositoryTest.make(effect, store)

    val service = DefaultProductService.make(effect, repository)

    describe("DefaultProductService.getBy") {
        it("Return a Product") {
            val result = service.getBy(product.id.toCore())
                .map { it.fromCore() }
                .map { it == product }
                .handleError { false }
                .fix().unsafeRunSync()
            assert(result)
        }
        it("Return a Error") {
            val result = service.getBy(ProductId().toCore())
                .map { it.fromCore() }
                .map { false }
                .handleError { true }
                .fix().unsafeRunSync()
            assert(result)
        }
    }

    describe("DefaultProductService.getAll") {
        it("Return a Product") {
            val result = service.getAll()
                .map { list -> list.map { it.fromCore() } }
                .map { it == listOf(product) }
                .handleError { false }
                .fix().unsafeRunSync()
            assert(result)
        }
    }

    describe("DefaultProductService.create") {
        it("Save a Product") {
            val newProduct = Product(
                name = ProductName("New Test Name"),
                stock = ProductStock(50.0)
            )
            val result = service.create(newProduct.toCore())
                .flatMap { service.getBy(newProduct.id.toCore()) }
                .map { it == newProduct.toCore() }
                .handleError { false }
                .fix().unsafeRunSync()
            assert(result)
        }
    }

    describe("DefaultProductService.deleteBy") {
        it("Delete a Product") {
            val result = service.deleteBy(product.id.toCore())
                .flatMap { service.getBy(product.id.toCore()) }
                .map { false }
                .handleError { true }
                .fix().unsafeRunSync()
            assert(result)
        }
    }
})
*/
