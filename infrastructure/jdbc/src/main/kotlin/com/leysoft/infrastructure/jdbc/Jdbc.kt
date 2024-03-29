package com.leysoft.infrastructure.jdbc

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.leysoft.core.error.InfrastructureException
import com.leysoft.infrastructure.jdbc.Jdbc.Instance.SqlException.Data
import com.leysoft.infrastructure.logger.Logger
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.Session
import com.vladsch.kotlin.jdbc.SqlQuery
import com.vladsch.kotlin.jdbc.Transaction
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface Jdbc {
    suspend fun <A> first(query: SqlQuery, decoder: Decoder<A>): Either<SqlException, A>
    suspend fun <A> list(query: SqlQuery, decoder: Decoder<A>): Either<SqlException, List<A>>
    suspend fun command(command: SqlQuery): Either<SqlException, Int>
    suspend fun <A> transaction(program: (Transaction) -> A): Either<SqlException, A>

    companion object Instance {
        private val log: Logger by lazy { Logger.get<Jdbc>() }

        sealed class SqlException(
            override val message: String
        ) : InfrastructureException(message) {
            companion object Data {
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
            }
        }

        interface Decoder<A> {
            fun decode(row: Row): A
        }

        context(Session, CoroutineContext)
        fun make(): Jdbc =
            object : Jdbc {
                override suspend fun <A> first(
                    query: SqlQuery,
                    decoder: Decoder<A>
                ): Either<SqlException, A> =
                    withContext(this@CoroutineContext) {
                        log.info("Start Jdbc.first(): ${query.statement}")
                        either<Throwable, A?> { first(query) { decoder.decode(it) } }
                            .mapLeft {
                                Data.QueryError(
                                    it.message ?: "Error trying to execute first"
                                )
                            }
                            .flatMap {
                                it?.right() ?: Data.SqlNotFound("Not Found").left()
                            }
                            .onLeft { error -> log.info("Error executing Jdbc.first(): $error") }
                            .onRight { log.info("End Jdbc.first(): ${query.statement}") }
                    }

                override suspend fun <A> list(query: SqlQuery, decoder: Decoder<A>): Either<SqlException, List<A>> =
                    withContext(this@CoroutineContext) {
                        log.info("Start Jdbc.list(): ${query.statement}")
                        either<Throwable, List<A>> { this@Session.list(query) { decoder.decode(it) } }
                            .mapLeft {
                                Data.QueryError(
                                    it.message ?: "Error trying to execute list"
                                )
                            }.onLeft { error -> log.info("Error executing Jdbc.list(): $error") }
                            .onRight { log.info("End Jdbc.list(): ${query.statement}") }
                    }

                override suspend fun command(command: SqlQuery): Either<SqlException, Int> =
                    withContext(this@CoroutineContext) {
                        transaction { it.update(command) }
                            .mapLeft { Data.CommandError(it.message) }
                    }

                override suspend fun <A> transaction(program: (Transaction) -> A): Either<SqlException, A> =
                    withContext(this@CoroutineContext) {
                        log.info("Start Jdbc.transaction()")
                        either<Throwable, A> { this@Session.transaction { program(it) } }
                            .mapLeft {
                                Data.TransactionError(
                                    it.message ?: "Error trying to execute transaction"
                                )
                            }
                            .onLeft { error -> log.info("Error executing Jdbc.transaction(): $error") }
                            .onRight { log.info("End Jdbc.transaction()") }
                    }
            }
    }
}
