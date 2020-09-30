package com.leysoft.products.adapter.out.persistence.sql

import arrow.Kind
import arrow.core.Option
import arrow.fx.typeclasses.Effect
import com.leysoft.infrastructure.jdbc.Jdbc
import com.leysoft.infrastructure.logger.Logger
import com.leysoft.infrastructure.logger.LoggerFactory
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductCreatedAt
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.ProductName
import com.leysoft.products.domain.ProductStock
import com.leysoft.products.domain.persistence.ProductRepository
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.sqlQuery

class SqlProductRepository<F> private constructor(
    private val Q: Effect<F>
) : ProductRepository<F>, Effect<F> by Q {

    private val log: Logger<F> = LoggerFactory.getLogger(Q, SqlProductRepository::class)

    override fun findBy(id: ProductId): Kind<F, Option<Product>> =
        log.info("Init findBy")
            .flatMap { Jdbc.option(Q, decoder, getById(id)) }
            .guarantee(log.info("End findBy"))
            .onError { error -> log.error("Error findBy: $error") }

    override fun findAll(): Kind<F, List<Product>> =
        Jdbc.list(Q, decoder, getAll())

    override fun save(product: Product): Kind<F, Unit> =
        Jdbc.command(Q, insert(product)).void()

    override fun deleteBy(id: ProductId): Kind<F, Boolean> =
        Jdbc.command(Q, delById(id)).map { it > 0 }

    companion object {

        private fun getById(id: ProductId) = sqlQuery(
            "SELECT * FROM products.products WHERE id = ?",
            id.value
        )

        private fun getAll() = sqlQuery("SELECT * FROM products.products")

        private fun insert(product: Product) = sqlQuery(
            "INSERT INTO products.products(id, name, stock, created_at)  VALUES (?, ?, ?, ?)",
            listOf(product.id.value, product.name.value, product.stock.value, product.createdAt.value)
        )

        private fun delById(id: ProductId) = sqlQuery(
            "DELETE FROM products.products WHERE id = ?",
            id.value
        )

        private val decoder: Jdbc.Decoder<Product> = object : Jdbc.Decoder<Product> {
            override fun decode(row: Row): Product =
                Product(
                    id = ProductId(row.string("id")),
                    name = ProductName(row.string("name")),
                    stock = ProductStock(row.double("stock")),
                    createdAt = ProductCreatedAt(row.offsetDateTime("created_at"))
                )
        }

        fun <F> make(Q: Effect<F>): ProductRepository<F> =
            SqlProductRepository(Q)
    }
}
