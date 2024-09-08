package com.leysoft.core.domain

import java.util.UUID

data class ContextId(
    val value: String,
) {
    companion object {
        fun make(): ContextId = ContextId(UUID.randomUUID().toString())
    }
}

data class Context(
    val id: ContextId,
) {
    companion object {
        fun make(): Context = Context(ContextId.make())

        fun from(id: ContextId): Context = Context(id)
    }
}
