package com.codepoetics.hume;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.codepoetics.hume.api.Property;
import com.codepoetics.hume.api.Record;
import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

public class PropertyDictionary {

    private static final CacheLoader<? super Class<?>, Map<Method, Property<?, ?>>> unboundLoader = new CacheLoader<Class<?>, Map<Method, Property<?, ?>>>() {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Map<Method, Property<?, ?>> load(Class<?> klass) throws Exception {
            Map<Method, Property<?, ?>> methodMap = Maps.newHashMap();
            for (Method method : klass.getDeclaredMethods()) {
                if (Property.class.isAssignableFrom(method
                        .getReturnType())) {
                    methodMap.put(method, new UnboundProperty(method.getName(), klass));
                }
            }
            return methodMap;
        }
    };
    
    private static final LoadingCache<Class<?>, Map<Method, Property<?, ?>>> unboundCache = CacheBuilder.newBuilder().build(unboundLoader);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <R extends Record<R>> Map<Method, Property<R, ?>> unboundPropertiesForType(Class<R> klass) {
        try {
            return (Map) unboundCache.get(klass);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <R extends Record<R>> Map<Method, Property<R, ?>> boundPropertiesForType(Class<R> klass, final Record<R> instance) {
        Map<Method, Property<R, ?>> unboundProperties = unboundPropertiesForType(klass);
        return Maps.transformValues(unboundProperties, new Function<Property<R, ?>, Property<R, ?>>() {
            @Override
            public Property<R, ?> apply(Property<R, ?> property) {
                return BoundProperty.bind(property, instance);
            }
        });
    }
}
