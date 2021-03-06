package com.leysoft.core.error

import java.lang.RuntimeException

abstract class ProductException(
    override val message: String
) : RuntimeException(message)

data class NotFoundProductException(
    override val message: String
) : ProductException(message)

data class CreateProductException(
    override val message: String
) : ProductException(message)

data class DeleteProductException(
    override val message: String
) : ProductException(message)
