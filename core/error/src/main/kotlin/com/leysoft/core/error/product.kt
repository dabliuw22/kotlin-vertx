package com.leysoft.core.error

abstract class BaseException(
    override val message: String
) :
    RuntimeException(message)

abstract class InfrastructureException(
    override val message: String
) : BaseException(message)

abstract class ProductException(
    override val message: String
) : BaseException(message)

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
