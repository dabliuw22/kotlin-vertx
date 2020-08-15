package com.leysoft.infrastructure.postgres

import arrow.Kind
import arrow.core.Option
import arrow.fx.typeclasses.Effect
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.Session
import com.vladsch.kotlin.jdbc.SqlQuery

object Postgres {

    interface Decoder<A> {
        fun decode(row: Row): A
    }

    fun <F, A> option(E: Effect<F>, decoder: Decoder<A>, session: Session, query: SqlQuery): Kind<F, Option<A>> =
        E.just(
            Option.fromNullable(
                session.first(query) { decoder.decode(it) }
            )
        )

    fun <F, A> list(E: Effect<F>, decoder: Decoder<A>, session: Session, query: SqlQuery): Kind<F, List<A>> =
        E.just(session.list(query) { decoder.decode(it) })

    fun <F> command(E: Effect<F>, session: Session, command: SqlQuery): Kind<F, Int> =
        E.just(session.update(command))
}
