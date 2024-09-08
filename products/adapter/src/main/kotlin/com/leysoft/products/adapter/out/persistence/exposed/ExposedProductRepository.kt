package com.leysoft.products.adapter.out.persistence.exposed

import arrow.core.identity
import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.fold
import com.leysoft.core.error.*
import com.leysoft.infrastructure.exposed.Exposed
import com.leysoft.infrastructure.exposed.Exposed.Companion.Decoder.Companion.decoder
import com.leysoft.infrastructure.exposed.Exposed.Companion.ExposedException
import com.leysoft.infrastructure.logger.Logger
import com.leysoft.products.domain.*
import com.leysoft.products.domain.persistence.ProductRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement

context(Exposed)
class ExposedProductRepository private constructor() : ProductRepository {

    context(Raise<ProductException>)
    override suspend fun findBy(id: ProductId): Product {
        log.info("Init findBy")
        val result =
            fold(
                block = { single(id.getById(), codec) },
                recover = { error: ExposedException ->
                    when (error) {
                        is Exposed.Companion.SqlNotFound ->
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
                recover = { error: ExposedException -> raise(CustomProductException(error.message)) },
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
                block = {
                    transaction {
                        insert(product)
                    }
                },
                recover = { error: ExposedException -> raise(CreateProductException(error.message)) },
                catch = { _ -> raise(CreateProductException()) },
                transform = ::identity,
            )
        log.info("End save")
        product.wasSaved(result.insertedCount)
        return product
    }

    context(Raise<ProductException>)
    override suspend fun deleteBy(id: ProductId) {
        log.info("Init deleteBy")
        val result =
            fold(
                block = { transaction { delete(id) } },
                recover = { error: ExposedException -> raise(DeleteProductException(error.message)) },
                catch = { _ -> raise(DeleteProductException()) },
                transform = ::identity,
            )
        id.wasDeleted(result)
        log.info("End deleteBy")
    }

    companion object {

        @JvmStatic
        private val log: Logger by lazy { Logger.get<ExposedProductRepository>() }

        private val codec: Exposed.Companion.Decoder<Product> =
            decoder {
                map {
                    Product(
                        id = ProductId(this[Products.id]),
                        name = ProductName(this[Products.name]),
                        stock = ProductStock(this[Products.stock]),
                        createdAt = ProductCreatedAt(this[Products.createdAt]),
                    )
                }
            }

        context(Raise<ProductException>)
        private fun Product.wasSaved(result: Int): Unit = ensure(result > 0) { CreateProductException("Not save Product: $this") }

        context(Raise<ProductException>)
        private fun ProductId.wasDeleted(result: Int): Unit = ensure(result > 0) { DeleteProductException("Not delete Product: $this") }

        private fun ProductId.getById(): Query =
            Products
                .select(Products.id, Products.name, Products.stock, Products.createdAt)
                .where { Products.id eq value }

        private val getAll: Query = Products.selectAll()

        private val insert: (Product) -> InsertStatement<Number> = { product: Product ->
            Products.insert {
                it[id] = product.id.value
                it[name] = product.name.value
                it[stock] = product.stock.value
                it[createdAt] = product.createdAt.value
            }
        }

        private val delete: (ProductId) -> Int = { id: ProductId ->
            Products.deleteWhere { Products.id eq id.value }
        }

        context(Exposed)
        operator fun invoke(): ProductRepository = ExposedProductRepository()
    }
}
