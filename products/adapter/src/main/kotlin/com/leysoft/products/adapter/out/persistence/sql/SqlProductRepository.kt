package com.leysoft.products.adapter.out.persistence.sql

import arrow.core.*
import arrow.core.raise.*
import com.leysoft.core.error.CreateProductException
import com.leysoft.core.error.CustomProductException
import com.leysoft.core.error.DeleteProductException
import com.leysoft.core.error.NotFoundProductException
import com.leysoft.core.error.ProductException
import com.leysoft.infrastructure.jdbc.Jdbc
import com.leysoft.infrastructure.jdbc.Jdbc.Companion.SqlNotFound
import com.leysoft.infrastructure.logger.Logger
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductCreatedAt
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.ProductName
import com.leysoft.products.domain.ProductStock
import com.leysoft.products.domain.persistence.ProductRepository
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.SqlQuery
import com.vladsch.kotlin.jdbc.sqlQuery

context(Jdbc)
class SqlProductRepository private constructor() :
    ProductRepository {

    context(Raise<ProductException>)
    override suspend fun findBy(id: ProductId): Product {
        log.info("Init findBy")
        val result = fold(
            block = { first(getById(id), decoder) },
            recover = { error: Jdbc.Companion.SqlException ->
                when (error) {
                    is SqlNotFound ->
                        raise(NotFoundProductException(error.message))
                    else -> raise(CustomProductException(error.message))
                }
            },
            transform = ::identity
        )
        log.info("End findBy")
        return result
    }

    context(Raise<ProductException>)
    override suspend fun findAll(): List<Product> {
        log.info("Init findAll")
        val result = fold(
            block = { list(getAll, decoder) },
            recover = { error: Jdbc.Companion.SqlException -> raise(CustomProductException(error.message)) },
            transform = ::identity
        )
        log.info("End findAll")
        return result
    }

    context(Raise<ProductException>)
    override suspend fun save(product: Product) {
        log.info("Init save")
        val result = fold(
            block = { command(insert(product)).await() },
            recover = { error: Jdbc.Companion.SqlException -> raise(CreateProductException(error.message)) },
            catch = { _ -> raise(CreateProductException()) },
            transform = ::identity
        )
        product.wasSaved(result)
        log.info("End save")
    }

    context(Raise<ProductException>)
    override suspend fun deleteBy(id: ProductId) {
        log.info("Init deleteBy")
        val result = fold(
            block = { command(delById(id)).await() },
            recover = { error: Jdbc.Companion.SqlException -> raise(DeleteProductException(error.message)) },
            catch = { _ -> raise(DeleteProductException()) },
            transform = ::identity
        )
        id.wasDeleted(result)
        log.info("End deleteBy")
    }

    companion object {
        @JvmStatic
        private val log: Logger by lazy { Logger.get<SqlProductRepository>() }

        context(Raise<ProductException>)
        private fun Product.wasSaved(result: Int): Unit =
            ensure(result <= 0) { CreateProductException("Not save Product: ${this.id}") }

        context(Raise<ProductException>)
        private fun ProductId.wasDeleted(result: Int): Unit =
            ensure(result <= 0) { DeleteProductException("Not delete Product: $this") }

        private val getById: (ProductId) -> SqlQuery = {
            sqlQuery(
                "SELECT * FROM products.products WHERE id = ?",
                it.value
            )
        }

        private val getAll: SqlQuery =
            sqlQuery("SELECT * FROM products.products")

        private val insert: (Product) -> SqlQuery = {
            sqlQuery(
                """INSERT INTO products.products(id, name, stock, created_at) 
                |VALUES (?, ?, ?, ?)
                """.trimMargin(),
                listOf(
                    it.id.value,
                    it.name.value,
                    it.stock.value,
                    it.createdAt.value
                )
            )
        }

        private val delById: (ProductId) -> SqlQuery = {
            sqlQuery(
                "DELETE FROM products.products WHERE id = ?",
                it.value
            )
        }

        private val decoder: Jdbc.Companion.Decoder<Product> = object : Jdbc.Companion.Decoder<Product> {
            override fun decode(row: Row): Product =
                Product(
                    id = ProductId(row.string("id")),
                    name = ProductName(row.string("name")),
                    stock = ProductStock(row.double("stock")),
                    createdAt = ProductCreatedAt(row.offsetDateTime("created_at"))
                )
        }

        context(Jdbc)
        operator fun invoke(): ProductRepository =
            SqlProductRepository()
    }
}
