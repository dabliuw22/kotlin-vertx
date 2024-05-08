package com.leysoft.infrastructure.jdbc

import arrow.core.raise.Raise
import arrow.core.raise.recover
import com.leysoft.core.error.InfrastructureException
import com.leysoft.infrastructure.logger.Logger
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.Session
import com.vladsch.kotlin.jdbc.SqlQuery
import com.vladsch.kotlin.jdbc.Transaction
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface Jdbc {
    context(Raise<SqlException>)
    suspend fun <A> first(query: SqlQuery, decoder: Decoder<A>): A

    context(Raise<SqlException>)
    suspend fun <A> list(query: SqlQuery, decoder: Decoder<A>): List<A>

    context(Raise<SqlException>)
    suspend fun command(command: SqlQuery): Deferred<Int>

    context(Raise<SqlException>)
    suspend fun <A> transaction(program: (Transaction) -> A): Deferred<A>

    companion object {
        private val log: Logger by lazy { Logger.get<Jdbc>() }

        sealed class SqlException(
            override val message: String
        ) : InfrastructureException(message)

        data class QueryError(
            override val message: String
        ) : SqlException(message)

        data class SqlNotFound(
            override val message: String
        ) : SqlException(message)

        data class CommandError(
            override val message: String
        ) : SqlException(message)

        data class TransactionError(
            override val message: String
        ) : SqlException(message)

        interface Decoder<A> {
            fun decode(row: Row): A
        }

        context(Session, CoroutineContext)
        operator fun invoke(): Jdbc =
            object : Jdbc {

                context(Raise<SqlException>)
                override suspend fun <A> first(
                    query: SqlQuery,
                    decoder: Decoder<A>
                ): A =
                    withContext(this@CoroutineContext) {
                        log.info("Start Jdbc.first(): ${query.statement}")
                        val result = recover(
                            block = { first(query) { decoder.decode(it) } ?: raise(SqlNotFound("Not Found")) },
                            recover = { error: Throwable ->
                                raise(
                                    QueryError(
                                        error.message ?: "Error trying to execute first"
                                    )
                                )
                            }
                        )
                        log.info("End Jdbc.list(): ${query.statement}")
                        result
                    }

                context(Raise<SqlException>)
                override suspend fun <A> list(query: SqlQuery, decoder: Decoder<A>): List<A> =
                    withContext(this@CoroutineContext) {
                        log.info("Start Jdbc.list(): ${query.statement}")
                        val result = recover(
                            block = { this@Session.list(query) { decoder.decode(it) } },
                            recover = { error: Throwable ->
                                raise(
                                    QueryError(
                                        error.message ?: "Error trying to execute list"
                                    )
                                )
                            }
                        )
                        log.info("End Jdbc.list(): ${query.statement}")
                        result
                    }

                context(Raise<SqlException>)
                override suspend fun command(command: SqlQuery): Deferred<Int> =
                    withContext(this@CoroutineContext) {
                        recover(
                            block = { transaction { it.update(command) } },
                            recover = { error: Throwable ->
                                raise(
                                    CommandError(
                                        error.message ?: "Error trying to execute command"
                                    )
                                )
                            }
                        )
                    }

                context(Raise<SqlException>)
                override suspend fun <A> transaction(program: (Transaction) -> A): Deferred<A> =
                    withContext(this@CoroutineContext) {
                        async {
                            log.info("Start Jdbc.transaction()")
                            val result = recover(
                                block = { this@Session.transaction { program(it) } },
                                recover = { error: Throwable ->
                                    raise(
                                        TransactionError(
                                            error.message ?: "Error trying to execute transaction"
                                        )
                                    )
                                }
                            )
                            log.info("End Jdbc.transaction()")
                            result
                        }
                    }
            }
    }
}
