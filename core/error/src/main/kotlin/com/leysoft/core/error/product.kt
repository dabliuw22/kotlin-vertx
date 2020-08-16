package com.leysoft.core.error

import java.lang.RuntimeException

data class NotFoundProductException(val msg: String) : RuntimeException(msg)

data class CreateProductException(val msg: String) : RuntimeException(msg)

data class DeleteProductException(val msg: String) : RuntimeException(msg)
