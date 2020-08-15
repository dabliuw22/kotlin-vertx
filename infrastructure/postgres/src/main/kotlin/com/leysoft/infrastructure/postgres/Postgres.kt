package com.leysoft.infrastructure.postgres

import arrow.Kind
import arrow.core.Option
import arrow.fx.typeclasses.Effect
import com.leysoft.infrastructure.postgres.config.PgConfig
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.SqlQuery

object Postgres {

    private val pgConfig = PgConfig(
        System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/vertx_db",
        System.getenv("DB_USER") ?: "vertx",
        System.getenv("DB_PASSWORD") ?: "vertx",
        System.getenv("DB_POOL_MAX_SIZE")?.toInt() ?: 10
    )

    interface Decoder<A> {
        fun decode(row: Row): A
    }

    fun <F, A> option(E: Effect<F>, decoder: Decoder<A>, query: SqlQuery): Kind<F, Option<A>> =
        pgConfig.resource(E).use { session ->
            E.just(Option.fromNullable(session.first(query) { row -> decoder.decode(row) }))
        }

    fun <F, A> list(E: Effect<F>, decoder: Decoder<A>, query: SqlQuery): Kind<F, List<A>> =
        pgConfig.resource(E).use { session ->
            E.just(session.list(query) { row -> decoder.decode(row) })
        }

    fun <F> command(E: Effect<F>, command: SqlQuery): Kind<F, Int> =
        pgConfig.resource(E).use { session ->
            E.just(session.transaction { it.update(command) })
        }
}
