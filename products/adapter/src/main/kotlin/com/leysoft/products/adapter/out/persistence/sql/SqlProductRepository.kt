package com.leysoft.products.adapter.out.persistence.sql

import arrow.core.Either
import arrow.core.filterOrOther
import com.leysoft.core.error.CreateProductException
import com.leysoft.core.error.CustomProductException
import com.leysoft.core.error.DeleteProductException
import com.leysoft.core.error.NotFoundProductException
import com.leysoft.core.error.ProductException
import com.leysoft.infrastructure.jdbc.Sql
import com.leysoft.infrastructure.jdbc.Sql.Instance.SqlException.Data
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

context(Sql)
class SqlProductRepository private constructor() :
    ProductRepository {

    private val log: Logger = Logger.get<SqlProductRepository>()

    override suspend fun findBy(id: ProductId): Either<ProductException, Product> =
        log.info("Init findBy").let { _ ->
            first(getById(id), decoder)
                .mapLeft {
                    when (val result = it) {
                        is Data.SqlNotFound ->
                            NotFoundProductException(result.message)
                        else -> CustomProductException(result.message)
                    }
                }
        }.also { log.info("End findBy") }

    override suspend fun findAll(): Either<ProductException, List<Product>> =
        list(getAll, decoder)
            .mapLeft { CustomProductException(it.message) }

    override suspend fun save(product: Product): Either<ProductException, Unit> =
        command(insert(product))
            .mapLeft { CreateProductException(it.message) }
            .filterOrOther({ it > 0 }) {
                CreateProductException("Not save Product: ${product.id}")
            }.map { }

    override suspend fun deleteBy(id: ProductId): Either<ProductException, Unit> =
        command(delById(id))
            .mapLeft { DeleteProductException(it.message) }
            .filterOrOther({ it > 0 }) {
                DeleteProductException("Not delete Product: $id")
            }.map { }

    companion object {

        private val getById: (ProductId) -> SqlQuery = {
            sqlQuery(
                "SELECT * FROM products.products WHERE id = ?",
                it.value
            )
        }

        private val getAll : SqlQuery =
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

        private val decoder: Sql.Instance.Decoder<Product> = object : Sql.Instance.Decoder<Product> {
            override fun decode(row: Row): Product =
                Product(
                    id = ProductId(row.string("id")),
                    name = ProductName(row.string("name")),
                    stock = ProductStock(row.double("stock")),
                    createdAt = ProductCreatedAt(row.offsetDateTime("created_at"))
                )
        }

        context(Sql)
        fun make(): ProductRepository =
            SqlProductRepository()
    }
}
