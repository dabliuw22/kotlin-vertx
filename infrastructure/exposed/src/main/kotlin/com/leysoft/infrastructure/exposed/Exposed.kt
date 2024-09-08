package com.leysoft.infrastructure.exposed

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.leysoft.core.error.InfrastructureException
import com.leysoft.infrastructure.logger.Logger
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.coroutines.CoroutineContext

interface Exposed {

    context(Raise<ExposedException>)
    suspend fun <A> single(
        query: Query,
        decoder: Decoder<A>,
    ): A

    context(Raise<ExposedException>)
    suspend fun <A> list(
        query: Query,
        decoder: Decoder<A>,
    ): List<A>

    context(Raise<ExposedException>)
    suspend fun <A> transaction(program: Transaction.() -> A): A

    companion object {
        private val log: Logger by lazy { Logger.get<Exposed>() }

        sealed class ExposedException(
            override val message: String,
        ) : InfrastructureException(message)

        data class QueryError(
            override val message: String,
        ) : ExposedException(message)

        data class SqlNotFound(
            override val message: String,
        ) : ExposedException(message)

        data class CommandError(
            override val message: String,
        ) : ExposedException(message)

        data class TransactionError(
            override val message: String,
        ) : ExposedException(message)

        data class InvalidStatementError(
            override val message: String = "Invalid SQL statement, it can not be NULL or empty",
        ) : ExposedException(message)

        data class InvalidResultSetError(
            override val message: String = "Invalid Result Set, it can not be NULL",
        ) : ExposedException(message)

        data class InvalidResultDecoderError(
            override val message: String = "Invalid Decoder, it can not be NULL",
        ) : ExposedException(message)

        data class Decoder<A>(
            val row: ResultRow? = null,
            val mapper: (ResultRow.() -> A)? = null,
        ) {

            fun result(row: ResultRow): Decoder<A> = this.copy(row = row)

            fun map(fa: ResultRow.() -> A): Decoder<A> = this.copy(mapper = fa)

            context(Raise<ExposedException>)
            fun decode(): A {
                val value = row ?: raise(InvalidResultSetError())
                val fa = mapper ?: raise(InvalidResultDecoderError())
                return fa.invoke(value)
            }

            companion object {
                fun <A> decoder(builder: Decoder<A>.() -> Decoder<A>): Decoder<A> = builder.invoke(Decoder())
            }
        }

        context(Database, CoroutineContext)
        operator fun invoke(): Exposed =
            object : Exposed {

                context(Raise<ExposedException>)
                override suspend fun <A> single(
                    query: Query,
                    decoder: Decoder<A>,
                ): A =
                    catch(
                        block = { transaction { query.map { decoder.result(it).decode() }.single() } },
                        catch = { error: Throwable ->
                            when (error) {
                                is NoSuchElementException -> raise(SqlNotFound("Not Found"))

                                else -> raise(
                                    QueryError(
                                        error.message ?: "Error trying to execute first",
                                    ),
                                )
                            }
                        },
                    )

                context(Raise<ExposedException>)
                override suspend fun <A> list(
                    query: Query,
                    decoder: Decoder<A>,
                ): List<A> =
                    catch(
                        block = { transaction { query.map { decoder.result(it).decode() } } },
                        catch = { error: Throwable ->
                            raise(
                                TransactionError(
                                    error.message ?: "Error trying to execute transaction",
                                ),
                            )
                        },
                    )

                context(Raise<ExposedException>)
                override suspend fun <A> transaction(program: Transaction.() -> A): A =
                    catch(
                        block = {
                            newSuspendedTransaction(
                                this@CoroutineContext,
                                this@Database,
                            ) { program() }
                        },
                        catch = { error: Throwable ->
                            when (error) {
                                is NoSuchElementException -> raise(SqlNotFound("Not Found"))

                                else -> raise(
                                    QueryError(
                                        error.message ?: "Error trying to execute first",
                                    ),
                                )
                            }
                        },
                    )
            }
    }
}
