package com.leysoft.core.error

import java.lang.RuntimeException

abstract class ProductException(msg: String) : RuntimeException(msg)

data class NotFoundProductException(val msg: String) : ProductException(msg)

data class CreateProductException(val msg: String) : ProductException(msg)

data class DeleteProductException(val msg: String) : ProductException(msg)
