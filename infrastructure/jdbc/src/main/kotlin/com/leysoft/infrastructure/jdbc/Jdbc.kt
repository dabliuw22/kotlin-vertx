package com.leysoft.infrastructure.jdbc

import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.recover
import arrow.optics.*
import com.leysoft.core.error.InfrastructureException
import com.leysoft.infrastructure.logger.Logger
import com.vladsch.kotlin.jdbc.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface Jdbc {
    context(Raise<SqlException>)
    suspend fun <A> first(
        query: Query,
        decoder: Decoder<A>,
    ): A

    context(Raise<SqlException>)
    suspend fun <A> list(
        query: Query,
        decoder: Decoder<A>,
    ): List<A>

    context(Raise<SqlException>)
    suspend fun command(command: Query): Deferred<Int>

    context(Raise<SqlException>)
    suspend fun <A> transaction(program: (Transaction) -> A): Deferred<A>

    companion object {
        private val log: Logger by lazy { Logger.get<Jdbc>() }

        sealed class SqlException(
            override val message: String,
        ) : InfrastructureException(message)

        data class QueryError(
            override val message: String,
        ) : SqlException(message)

        data class SqlNotFound(
            override val message: String,
        ) : SqlException(message)

        data class CommandError(
            override val message: String,
        ) : SqlException(message)

        data class TransactionError(
            override val message: String,
        ) : SqlException(message)

        data class InvalidStatementError(
            override val message: String = "Invalid SQL statement, it can not be NULL or empty",
        ) : SqlException(message)

        data class InvalidResultSetError(
            override val message: String = "Invalid Result Set, it can not be NULL",
        ) : SqlException(message)

        data class InvalidResultDecoderError(
            override val message: String = "Invalid Decoder, it can not be NULL",
        ) : SqlException(message)

        data class Decoder<A>(
            val row: Row? = null,
            val mapper: (Row.() -> A)? = null,
        ) {

            fun result(row: Row): Decoder<A> = this.copy(row = row)

            fun map(fa: Row.() -> A): Decoder<A> = this.copy(mapper = fa)

            context(Raise<SqlException>)
            fun decode(): A {
                val value = row ?: raise(InvalidResultSetError())
                val fa = mapper ?: raise(InvalidResultDecoderError())
                return fa.invoke(value)
            }

            companion object {
                fun <A> decoder(builder: Decoder<A>.() -> Decoder<A>): Decoder<A> = builder.invoke(Decoder())
            }
        }

        @optics
        data class QueryTemplate(
            val value: String,
        ) {
            companion object {
                fun String.template(): QueryTemplate = QueryTemplate(this)
            }
        }

        @optics
        data class QueryParameter(
            val value: Any,
        ) {
            companion object {
                fun Any.parameter(): QueryParameter = QueryParameter(this)
            }
        }

        @optics
        data class Query(
            val template: QueryTemplate? = null,
            val params: List<QueryParameter> = emptyList(),
        ) {

            fun add(item: QueryParameter): Query = Query.params.modify(this) { it + item }

            fun template(template: QueryTemplate): Query = Query.template.set(this, template)

            context(Raise<SqlException>)
            private fun sql(): String = template?.value ?: raise(InvalidStatementError())

            context(Raise<SqlException>)
            fun build(): SqlQuery =
                if (params.isEmpty()) {
                    SqlQuery(sql())
                } else {
                    SqlQuery(sql(), params.toList())
                }

            companion object {
                fun query(builder: Query.() -> Query): Query = builder.invoke(Query())
            }
        }

        context(Session, CoroutineContext)
        operator fun invoke(): Jdbc =
            object : Jdbc {
                context(Raise<SqlException>)
                override suspend fun <A> first(
                    query: Query,
                    decoder: Decoder<A>,
                ): A =
                    withContext(this@CoroutineContext) {
                        val sql = query.build()
                        log.info("Start Jdbc.first(): ${sql.statement}")
                        val result =
                            catch(
                                block = { first(sql) { decoder.result(it).decode() } ?: raise(SqlNotFound("Not Found")) },
                                catch = { error: Throwable ->
                                    raise(
                                        QueryError(
                                            error.message ?: "Error trying to execute first",
                                        ),
                                    )
                                },
                            )
                        log.info("End Jdbc.list(): ${sql.statement}")
                        result
                    }

                context(Raise<SqlException>)
                override suspend fun <A> list(
                    query: Query,
                    decoder: Decoder<A>,
                ): List<A> =
                    withContext(this@CoroutineContext) {
                        val sql = query.build()
                        log.info("Start Jdbc.list(): ${sql.statement}")
                        val result =
                            catch(
                                block = { this@Session.list(sql) { decoder.result(it).decode() } },
                                catch = { error: Throwable ->
                                    raise(
                                        QueryError(
                                            error.message ?: "Error trying to execute list",
                                        ),
                                    )
                                },
                            )
                        log.info("End Jdbc.list(): ${sql.statement}")
                        result
                    }

                context(Raise<SqlException>)
                override suspend fun command(command: Query): Deferred<Int> =
                    withContext(this@CoroutineContext) {
                        recover(
                            block = { transaction { it.update(command.build()) } },
                            recover = { error: SqlException -> raise(CommandError(error.message)) },
                            catch = { error: Throwable ->
                                raise(
                                    CommandError(
                                        error.message ?: "Error trying to execute command",
                                    ),
                                )
                            },
                        )
                    }

                context(Raise<SqlException>)
                override suspend fun <A> transaction(program: (Transaction) -> A): Deferred<A> =
                    withContext(this@CoroutineContext) {
                        async {
                            log.info("Start Jdbc.transaction()")
                            val result =
                                catch(
                                    block = { this@Session.transaction { program(it) } },
                                    catch = { error: Throwable ->
                                        raise(
                                            TransactionError(
                                                error.message ?: "Error trying to execute transaction",
                                            ),
                                        )
                                    },
                                )
                            log.info("End Jdbc.transaction()")
                            result
                        }
                    }
            }
    }
}
