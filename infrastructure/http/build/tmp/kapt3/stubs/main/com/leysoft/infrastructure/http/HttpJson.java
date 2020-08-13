package com.leysoft.infrastructure.http;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0005\u00a2\u0006\u0002\u0010\u0002J+\u0010\u0003\u001a\u0002H\u0004\"\b\b\u0000\u0010\u0004*\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00040\b\u00a2\u0006\u0002\u0010\tJ\u001d\u0010\n\u001a\u00020\u0006\"\b\b\u0000\u0010\u0004*\u00020\u00012\u0006\u0010\u0005\u001a\u0002H\u0004\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\r"}, d2 = {"Lcom/leysoft/infrastructure/http/HttpJson;", "", "()V", "decode", "A", "data", "", "clazz", "Lkotlin/reflect/KClass;", "(Ljava/lang/String;Lkotlin/reflect/KClass;)Ljava/lang/Object;", "encode", "(Ljava/lang/Object;)Ljava/lang/String;", "Companion", "infrastructure-http"})
public class HttpJson {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ApplicationJson = "application/json; charset=utf-8";
    public static final com.leysoft.infrastructure.http.HttpJson.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final <A extends java.lang.Object>java.lang.String encode(@org.jetbrains.annotations.NotNull()
    A data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final <A extends java.lang.Object>A decode(@org.jetbrains.annotations.NotNull()
    java.lang.String data, @org.jetbrains.annotations.NotNull()
    kotlin.reflect.KClass<A> clazz) {
        return null;
    }
    
    public HttpJson() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/leysoft/infrastructure/http/HttpJson$Companion;", "", "()V", "ApplicationJson", "", "infrastructure-http"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}