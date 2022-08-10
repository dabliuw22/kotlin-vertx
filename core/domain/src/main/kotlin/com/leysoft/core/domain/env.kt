package com.leysoft.core.domain

import arrow.fx.coroutines.Resource

interface FromEnv<A> {
    fun load(): Resource<A>
}
