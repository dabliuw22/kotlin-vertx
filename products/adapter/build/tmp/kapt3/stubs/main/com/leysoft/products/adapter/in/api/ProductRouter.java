package com.leysoft.products.adapter.in.api;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0002J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0002J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0002J\u0010\u0010\f\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0002J\u000e\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000fR\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/leysoft/products/adapter/in/api/ProductRouter;", "Lcom/leysoft/infrastructure/http/HttpJson;", "service", "Lcom/leysoft/products/application/ProductService;", "Larrow/fx/ForIO;", "(Lcom/leysoft/products/application/ProductService;)V", "allHandler", "", "ctx", "Lio/vertx/ext/web/RoutingContext;", "createHandler", "delByIdHandler", "getByIdHandler", "routers", "router", "Lio/vertx/ext/web/Router;", "Companion", "products-adapter"})
public final class ProductRouter extends com.leysoft.infrastructure.http.HttpJson {
    private final com.leysoft.products.application.ProductService<arrow.fx.ForIO> service = null;
    private static final java.lang.String Products = "/products";
    private static final java.lang.String GetAll = "/products";
    private static final java.lang.String GetById = "/products/:productId";
    private static final java.lang.String Create = "/products";
    private static final java.lang.String DelById = "/products/:productId";
    public static final com.leysoft.products.adapter.in.api.ProductRouter.Companion Companion = null;
    
    public final void routers(@org.jetbrains.annotations.NotNull()
    io.vertx.ext.web.Router router) {
    }
    
    private final void allHandler(io.vertx.ext.web.RoutingContext ctx) {
    }
    
    private final void getByIdHandler(io.vertx.ext.web.RoutingContext ctx) {
    }
    
    private final void createHandler(io.vertx.ext.web.RoutingContext ctx) {
    }
    
    private final void delByIdHandler(io.vertx.ext.web.RoutingContext ctx) {
    }
    
    public ProductRouter(@org.jetbrains.annotations.NotNull()
    com.leysoft.products.application.ProductService<arrow.fx.ForIO> service) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/leysoft/products/adapter/in/api/ProductRouter$Companion;", "", "()V", "Create", "", "DelById", "GetAll", "GetById", "Products", "products-adapter"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}