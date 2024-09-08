package com.leysoft.products.adapter.out.persistence.sql

import arrow.core.*
import arrow.core.raise.*
import com.leysoft.core.error.CreateProductException
import com.leysoft.core.error.CustomProductException
import com.leysoft.core.error.DeleteProductException
import com.leysoft.core.error.NotFoundProductException
import com.leysoft.core.error.ProductException
import com.leysoft.infrastructure.jdbc.Jdbc
import com.leysoft.infrastructure.jdbc.Jdbc.Companion.Decoder.Companion.decoder
import com.leysoft.infrastructure.jdbc.Jdbc.Companion.Query.Companion.query
import com.leysoft.infrastructure.jdbc.Jdbc.Companion.QueryParameter.Companion.parameter
import com.leysoft.infrastructure.jdbc.Jdbc.Companion.QueryTemplate.Companion.template
import com.leysoft.infrastructure.jdbc.Jdbc.Companion.SqlNotFound
import com.leysoft.infrastructure.logger.Logger
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductCreatedAt
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.ProductName
import com.leysoft.products.domain.ProductStock
import com.leysoft.products.domain.persistence.ProductRepository

context(Jdbc)
class SqlProductRepository private constructor() : ProductRepository {
    context(Raise<ProductException>)
    override suspend fun findBy(id: ProductId): Product {
        log.info("Init findBy")
        val result =
            fold(
                block = { first(id.getById(), codec) },
                recover = { error: Jdbc.Companion.SqlException ->
                    when (error) {
                        is SqlNotFound ->
                            raise(NotFoundProductException(error.message))

                        else -> raise(CustomProductException(error.message))
                    }
                },
                transform = ::identity,
            )
        log.info("End findBy")
        return result
    }

    context(Raise<ProductException>)
    override suspend fun findAll(): List<Product> {
        log.info("Init findAll")
        val result =
            fold(
                block = { list(getAll, codec) },
                recover = { error: Jdbc.Companion.SqlException -> raise(CustomProductException(error.message)) },
                transform = ::identity,
            )
        log.info("End findAll")
        return result
    }

    context(Raise<ProductException>)
    override suspend fun save(product: Product): Product {
        log.info("Init save")
        val result =
            fold(
                block = { command(product.insert()) },
                recover = { error: Jdbc.Companion.SqlException -> raise(CreateProductException(error.message)) },
                catch = { _ -> raise(CreateProductException()) },
                transform = ::identity,
            )
        val status = result.await()
        product.wasSaved(status)
        log.info("End save")
        return product
    }

    context(Raise<ProductException>)
    override suspend fun deleteBy(id: ProductId) {
        log.info("Init deleteBy")
        val result =
            fold(
                block = { command(id.delById()).await() },
                recover = { error: Jdbc.Companion.SqlException -> raise(DeleteProductException(error.message)) },
                catch = { _ -> raise(DeleteProductException()) },
                transform = ::identity,
            )
        id.wasDeleted(result)
        log.info("End deleteBy")
    }

    companion object {
        @JvmStatic
        private val log: Logger by lazy { Logger.get<SqlProductRepository>() }

        context(Raise<ProductException>)
        private fun Product.wasSaved(result: Int): Unit = ensure(result > 0) { CreateProductException("Not save Product: $this") }

        context(Raise<ProductException>)
        private fun ProductId.wasDeleted(result: Int): Unit = ensure(result > 0) { DeleteProductException("Not delete Product: $this") }

        private fun ProductId.getById(): Jdbc.Companion.Query =
            query {
                template(
                    "SELECT * FROM products.products WHERE id = ?"
                        .template(),
                ).add(value.parameter())
            }

        private val getAll: Jdbc.Companion.Query =
            query {
                template("SELECT * FROM products.products".template())
            }

        private fun Product.insert(): Jdbc.Companion.Query =
            query {
                template(
                    """INSERT INTO products.products(id, name, stock, created_at) 
                   |VALUES (?, ?, ?, ?)
                    """.trimMargin().template(),
                ).add(id.value.parameter())
                    .add(name.value.parameter())
                    .add(stock.value.parameter())
                    .add(createdAt.value.parameter())
            }

        private fun ProductId.delById(): Jdbc.Companion.Query =
            query {
                template("DELETE FROM products.products WHERE id = ?".template()).add(value.parameter())
            }

        private val codec: Jdbc.Companion.Decoder<Product> =
            decoder {
                map {
                    Product(
                        id = ProductId(this.string("id")),
                        name = ProductName(this.string("name")),
                        stock = ProductStock(this.double("stock")),
                        createdAt = ProductCreatedAt(this.offsetDateTime("created_at")),
                    )
                }
            }

        context(Jdbc)
        operator fun invoke(): ProductRepository = SqlProductRepository()
    }
}
