package com.leysoft.products.application;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J\u001c\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007H&J\u001a\u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0004H&J\"\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\f0\u00042\u0006\u0010\u0006\u001a\u00020\u0007H&J\u001c\u0010\r\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u000e0\u00042\u0006\u0010\u000f\u001a\u00020\nH&\u00a8\u0006\u0010"}, d2 = {"Lcom/leysoft/products/application/ProductService;", "F", "", "deleteBy", "Larrow/Kind;", "", "id", "Lcom/leysoft/core/domain/ProductId;", "findAll", "", "Lcom/leysoft/core/domain/Product;", "findBy", "Larrow/core/Option;", "save", "", "product", "products-application"})
public abstract interface ProductService<F extends java.lang.Object> {
    
    @org.jetbrains.annotations.NotNull()
    public abstract arrow.Kind<F, arrow.core.Option<com.leysoft.core.domain.Product>> findBy(@org.jetbrains.annotations.NotNull()
    com.leysoft.core.domain.ProductId id);
    
    @org.jetbrains.annotations.NotNull()
    public abstract arrow.Kind<F, java.util.List<com.leysoft.core.domain.Product>> findAll();
    
    @org.jetbrains.annotations.NotNull()
    public abstract arrow.Kind<F, kotlin.Unit> save(@org.jetbrains.annotations.NotNull()
    com.leysoft.core.domain.Product product);
    
    @org.jetbrains.annotations.NotNull()
    public abstract arrow.Kind<F, java.lang.Boolean> deleteBy(@org.jetbrains.annotations.NotNull()
    com.leysoft.core.domain.ProductId id);
}