package com.leysoft.infrastructure.jdbc

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.leysoft.core.error.InfrastructureException
import com.leysoft.infrastructure.jdbc.Jdbc.Instance.SqlException.Data
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.Session
import com.vladsch.kotlin.jdbc.SqlQuery
import com.vladsch.kotlin.jdbc.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Jdbc {
    suspend fun <A> first(query: SqlQuery, decoder: Decoder<A>): Either<SqlException, A>
    suspend fun <A> list(query: SqlQuery, decoder: Decoder<A>): Either<SqlException, List<A>>
    suspend fun command(command: SqlQuery): Either<SqlException, Int>
    suspend fun <A> transaction(program: (Transaction) -> A): Either<SqlException, A>

    companion object Instance {
        abstract class SqlException(
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

        context(Session)
        fun make(): Jdbc =
            object : Jdbc {
                override suspend fun <A> first(
                    query: SqlQuery,
                    decoder: Decoder<A>
                ): Either<SqlException, A> =
                    withContext(Dispatchers.IO) {
                        Either.catch(
                            {
                                Data.QueryError(it.message ?: "Error trying to execute first")
                            }
                        ) {
                            first(query) { decoder.decode(it) }
                        }.flatMap { it?.right() ?: Either.Left(Data.SqlNotFound("Not Found")) }
                    }

                override suspend fun <A> list(query: SqlQuery, decoder: Decoder<A>): Either<SqlException, List<A>> =
                    withContext(Dispatchers.IO) {
                        Either.catch({
                            Data.QueryError(it.message ?: "Error trying to execute list")
                        }) { this@Session.list(query) { decoder.decode(it) } }
                    }

                override suspend fun command(command: SqlQuery): Either<SqlException, Int> =
                    withContext(Dispatchers.IO) {
                        transaction { it.update(command) }
                            .mapLeft { Data.CommandError(it.message) }
                    }

                override suspend fun <A> transaction(program: (Transaction) -> A): Either<SqlException, A> =
                    withContext(Dispatchers.IO) {
                        Either.catch({
                            Data.TransactionError(it.message ?: "Error trying to execute transaction")
                        }) { this@Session.transaction { program(it) } }
                    }
            }
    }
}
