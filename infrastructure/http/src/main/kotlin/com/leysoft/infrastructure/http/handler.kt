package com.leysoft.infrastructure.http

import arrow.Kind
import arrow.core.Either
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.fix
import arrow.fx.reactor.FluxK
import arrow.fx.reactor.ForFluxK
import arrow.fx.reactor.ForMonoK
import arrow.fx.reactor.MonoK
import arrow.fx.reactor.fix
import arrow.fx.rx2.FlowableK
import arrow.fx.rx2.ForFlowableK
import arrow.fx.rx2.ForMaybeK
import arrow.fx.rx2.ForObservableK
import arrow.fx.rx2.ForSingleK
import arrow.fx.rx2.MaybeK
import arrow.fx.rx2.ObservableK
import arrow.fx.rx2.SingleK
import arrow.fx.rx2.fix

fun IO.Companion.handler(): HttpHandler<ForIO> = object : HttpHandler<ForIO> {
    override fun <A> Kind<ForIO, A>.handle(
        success: (A) -> Unit,
        failure: (Throwable) -> Unit
    ) = this.fix().unsafeRunAsync {
        when (it) {
            is Either.Right -> success(it.b)
            is Either.Left -> failure(it.a)
        }
    }
}

fun MonoK.Companion.handler(): HttpHandler<ForMonoK> = object : HttpHandler<ForMonoK> {
    override fun <A> Kind<ForMonoK, A>.handle(
        success: (A) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        this.fix().mono.subscribe(success, failure)
    }
}

fun FluxK.Companion.handler(): HttpHandler<ForFluxK> = object : HttpHandler<ForFluxK> {
    override fun <A> Kind<ForFluxK, A>.handle(
        success: (A) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        this.fix().flux.subscribe(success, failure)
    }
}

fun ObservableK.Companion.handler(): HttpHandler<ForObservableK> = object : HttpHandler<ForObservableK> {
    override fun <A> Kind<ForObservableK, A>.handle(
        success: (A) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        this.fix().observable.subscribe(success, failure)
    }
}

fun SingleK.Companion.handler(): HttpHandler<ForSingleK> = object : HttpHandler<ForSingleK> {
    override fun <A> Kind<ForSingleK, A>.handle(
        success: (A) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        this.fix().single.subscribe(success, failure)
    }
}

fun MaybeK.Companion.handler(): HttpHandler<ForMaybeK> = object : HttpHandler<ForMaybeK> {
    override fun <A> Kind<ForMaybeK, A>.handle(
        success: (A) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        this.fix().maybe.subscribe(success, failure)
    }
}

fun FlowableK.Companion.handler(): HttpHandler<ForFlowableK> = object : HttpHandler<ForFlowableK> {
    override fun <A> Kind<ForFlowableK, A>.handle(
        success: (A) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        this.fix().flowable.subscribe(success, failure)
    }
}
