package com.leysoft.products.adapter.out.persistence.memory

import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.fx.coroutines.Atomic
import com.leysoft.core.error.CreateProductException
import com.leysoft.core.error.DeleteProductException
import com.leysoft.core.error.NotFoundProductException
import com.leysoft.core.error.ProductException
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.persistence.ProductRepository

typealias Storage = Atomic<Map<String, Product>>

class InMemoryProductRepository private constructor(
    private val storage: Storage,
) : ProductRepository {
    context(Raise<ProductException>)
    override suspend fun findBy(id: ProductId): Product =
        catch(
            block = { storage.get()[id.value] ?: raise(NotFoundProductException("Not found product: $id")) },
            catch = { raise(NotFoundProductException("Not found product: $id")) },
        )

    context(Raise<ProductException>)
    override suspend fun findAll(): List<Product> =
        catch(
            block = { storage.get().values.toList() },
            catch = { raise(NotFoundProductException("Not found products")) },
        )

    context(Raise<ProductException>)
    override suspend fun save(product: Product): Product {
        val result = findBy(product.id)
        catch(
            block = {
                storage.update { it.plus(Pair(result.id.value, product)) }
            },
            catch = { raise(CreateProductException("Not save Product: ${product.id}")) },
        )
        return product
    }

    context(Raise<ProductException>)
    override suspend fun deleteBy(id: ProductId) {
        val result = findBy(id)
        catch(
            block = {
                storage.update { it.minus(result.id.value) }
            },
            catch = { raise(DeleteProductException("Not delete Product: $id")) },
        )
    }

    companion object {
        operator fun invoke(storage: Storage): ProductRepository = InMemoryProductRepository(storage)
    }
}
