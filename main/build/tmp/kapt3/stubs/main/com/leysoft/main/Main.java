package com.leysoft.main;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u0007H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u00060\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000"}, d2 = {"Lcom/leysoft/main/Main;", "Lcom/leysoft/infrastructure/http/HttpServer;", "()V", "Q", "Larrow/fx/extensions/IOEffect;", "db", "", "", "Lcom/leysoft/products/domain/Product;", "initId", "store", "Larrow/fx/Ref;", "Larrow/fx/ForIO;", "host", "port", "", "products", "", "router", "Lio/vertx/ext/web/Router;", "routes", "Companion"})
public final class Main extends com.leysoft.infrastructure.http.HttpServer {
    private final arrow.fx.extensions.IOEffect Q = null;
    private final java.lang.String initId = null;
    private final java.util.Map<java.lang.String, com.leysoft.products.domain.Product> db = null;
    private final arrow.fx.Ref<arrow.fx.ForIO, java.util.Map<java.lang.String, com.leysoft.products.domain.Product>> store = null;
    public static final com.leysoft.main.Main.Companion Companion = null;
    
    @java.lang.Override()
    public int port() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String host() {
        return null;
    }
    
    @java.lang.Override()
    public void routes(@org.jetbrains.annotations.NotNull()
    io.vertx.ext.web.Router router) {
    }
    
    private final void products(io.vertx.ext.web.Router router) {
    }
    
    public Main() {
        super();
    }
    
    public static final void main(@org.jetbrains.annotations.NotNull()
    java.lang.String[] args) {
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0007\u00a2\u0006\u0002\u0010\b"}, d2 = {"Lcom/leysoft/main/Main$Companion;", "", "()V", "main", "", "args", "", "", "([Ljava/lang/String;)V"})
    public static final class Companion {
        
        public final void main(@org.jetbrains.annotations.NotNull()
        java.lang.String[] args) {
        }
        
        private Companion() {
            super();
        }
    }
}