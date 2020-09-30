package com.leysoft.infrastructure.http

import arrow.Kind

interface HttpHandler<F> {

    fun <A> Kind<F, A>.handle(
        success: (A) -> Unit,
        failure: (Throwable) -> Unit
    )
}
