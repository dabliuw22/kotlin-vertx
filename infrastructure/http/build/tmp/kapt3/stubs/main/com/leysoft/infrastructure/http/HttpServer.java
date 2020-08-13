package com.leysoft.infrastructure.http;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\b\u0010\u0007\u001a\u00020\bH&J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH&J\u0018\u0010\r\u001a\u00020\n2\u000e\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fH\u0016\u00a8\u0006\u0011"}, d2 = {"Lcom/leysoft/infrastructure/http/HttpServer;", "Lio/vertx/core/AbstractVerticle;", "()V", "host", "", "options", "Lio/vertx/core/http/HttpServerOptions;", "port", "", "routes", "", "router", "Lio/vertx/ext/web/Router;", "start", "startFuture", "Lio/vertx/core/Future;", "Ljava/lang/Void;", "infrastructure-http"})
public abstract class HttpServer extends io.vertx.core.AbstractVerticle {
    
    @java.lang.Override()
    public void start(@org.jetbrains.annotations.Nullable()
    io.vertx.core.Future<java.lang.Void> startFuture) {
    }
    
    private final io.vertx.core.http.HttpServerOptions options() {
        return null;
    }
    
    public abstract int port();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String host();
    
    public abstract void routes(@org.jetbrains.annotations.NotNull()
    io.vertx.ext.web.Router router);
    
    public HttpServer() {
        super();
    }
}