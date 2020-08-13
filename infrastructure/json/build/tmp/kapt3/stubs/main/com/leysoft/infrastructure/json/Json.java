package com.leysoft.infrastructure.json;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u000fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J+\u0010\u0006\u001a\u0002H\u0007\"\b\b\u0000\u0010\u0007*\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00070\u000b\u00a2\u0006\u0002\u0010\fJ\u001d\u0010\r\u001a\u00020\t\"\b\b\u0000\u0010\u0007*\u00020\u00012\u0006\u0010\b\u001a\u0002H\u0007\u00a2\u0006\u0002\u0010\u000eR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/leysoft/infrastructure/json/Json;", "", "()V", "mapper", "Lcom/fasterxml/jackson/databind/ObjectMapper;", "kotlin.jvm.PlatformType", "read", "A", "data", "", "clazz", "Lkotlin/reflect/KClass;", "(Ljava/lang/String;Lkotlin/reflect/KClass;)Ljava/lang/Object;", "write", "(Ljava/lang/Object;)Ljava/lang/String;", "JsonException", "infrastructure-json"})
public final class Json {
    private static final com.fasterxml.jackson.databind.ObjectMapper mapper = null;
    public static final com.leysoft.infrastructure.json.Json INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    public final <A extends java.lang.Object>java.lang.String write(@org.jetbrains.annotations.NotNull()
    A data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final <A extends java.lang.Object>A read(@org.jetbrains.annotations.NotNull()
    java.lang.String data, @org.jetbrains.annotations.NotNull()
    kotlin.reflect.KClass<A> clazz) {
        return null;
    }
    
    private Json() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00060\u0001j\u0002`\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\u0004H\u00c6\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/leysoft/infrastructure/json/Json$JsonException;", "Ljava/lang/RuntimeException;", "Lkotlin/RuntimeException;", "throwable", "", "(Ljava/lang/Throwable;)V", "getThrowable", "()Ljava/lang/Throwable;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "infrastructure-json"})
    public static final class JsonException extends java.lang.RuntimeException {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.Throwable throwable = null;
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.Throwable getThrowable() {
            return null;
        }
        
        public JsonException(@org.jetbrains.annotations.NotNull()
        java.lang.Throwable throwable) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.Throwable component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.leysoft.infrastructure.json.Json.JsonException copy(@org.jetbrains.annotations.NotNull()
        java.lang.Throwable throwable) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public java.lang.String toString() {
            return null;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object p0) {
            return false;
        }
    }
}