package com.leysoft.core.error

sealed interface BaseException

open class InfrastructureException(
    open val message: String
) : BaseException

open class ProductException(
    open val message: String
) : BaseException

data class NotFoundProductException(
    override val message: String
) : ProductException(message)

data class CreateProductException(
    override val message: String
) : ProductException(message)

data class DeleteProductException(
    override val message: String
) : ProductException(message)

data class CustomProductException(
    override val message: String
) : ProductException(message)
