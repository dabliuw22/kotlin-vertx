package com.leysoft.infrastructure.jdbc

import arrow.Kind
import arrow.core.Option
import arrow.core.Try
import arrow.fx.typeclasses.Effect
import com.leysoft.infrastructure.jdbc.config.JdbcConfig
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.SqlQuery
import com.vladsch.kotlin.jdbc.Transaction
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object Jdbc {

    private val io: CoroutineContext = Dispatchers.IO

    interface Decoder<A> {
        fun decode(row: Row): A
    }

    fun <F, A> option(
        E: Effect<F>,
        decoder: Decoder<A>,
        query: SqlQuery
    ): Kind<F, Option<A>> =
        jdbcConfig.resource(E).use { session ->
            E.later(io) {
                val result = session.first(query) { decoder.decode(it) }
                Option.fromNullable(result)
            }
        }

    fun <F, A> list(
        E: Effect<F>,
        decoder: Decoder<A>,
        query: SqlQuery
    ): Kind<F, List<A>> =
        jdbcConfig.resource(E).use { session ->
            E.run {
                laterOrRaise(io) {
                    Try {
                        session.list(query) { decoder.decode(it) }
                    }.toEither()
                }
            }
        }

    fun <F> command(
        E: Effect<F>,
        command: SqlQuery
    ): Kind<F, Int> =
        transaction(E) { transaction -> transaction.update(command) }

    fun <F, A> transaction(
        E: Effect<F>,
        program: (Transaction) -> A
    ): Kind<F, A> =
        jdbcConfig.resource(E).use { session ->
            E.later(io) { session.transaction { program(it) } }
        }

    private val jdbcConfig = JdbcConfig(
        System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/vertx_db",
        System.getenv("DB_USER") ?: "vertx",
        System.getenv("DB_PASSWORD") ?: "vertx",
        System.getenv("DB_DRIVER") ?: "org.postgresql.Driver",
        System.getenv("DB_POOL_MAX_SIZE")?.toInt() ?: 10
    )
}
