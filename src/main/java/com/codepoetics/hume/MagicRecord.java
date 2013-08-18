package com.codepoetics.hume;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import com.codepoetics.hume.api.Builder;
import com.codepoetics.hume.api.Property;
import com.codepoetics.hume.api.PropertyMap;
import com.codepoetics.hume.api.Record;
import com.google.common.collect.Maps;

final class MagicRecord {

    private MagicRecord() {
    }

    public static interface TypeBinder<R extends Record<R>> {
        R with(PropertyMap<R> properties);
    }

    public static <R extends Record<R>> TypeBinder<R> ofType(
            final Class<R> recordType) {
        return new TypeBinder<R>() {
            @SuppressWarnings("unchecked")
            @Override
            public R with(PropertyMap<R> properties) {
                return (R) Proxy.newProxyInstance(recordType.getClassLoader(),
                        new Class<?>[] { recordType },
                        new BoundProxyInvocationHandler<R>(recordType,
                                properties));
            }
        };
    }

    private static class BoundProxyInvocationHandler<R extends Record<R>>
            implements Record<R>, InvocationHandler {

        private PropertyMap<R> properties;
        private Class<R> recordType;
        private Map<Method, Property<R, ?>> methodMap = Maps.newHashMap();

        public BoundProxyInvocationHandler(Class<R> recordType,
                PropertyMap<R> properties) {
            this.properties = properties.immutableCopy();
            this.recordType = recordType;
            this.methodMap = PropertyDictionary.boundPropertiesForType(recordType, this);
        }

        @Override
        public Object invoke(Object target, Method method, Object[] arguments)
                throws Throwable {
            if (methodMap.containsKey(method)) {
                return methodMap.get(method);
            }
            return method.invoke(this, arguments);
        }

        @Override
        public <T> T get(Property<R, T> property) {
            return properties.get(property);
        }

        @Override
        public PropertyMap<R> properties() {
            return properties;
        }
        
        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object other) {
            if (this == other) { return true; }
            if (other == null) { return false; }
            if (other instanceof Record) {
                return properties.equals(((Record) other).properties());
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return properties.hashCode();
        }

        @Override
        public <T> Builder<R> with(Property<R, T> property, T value) {
            return MagicBuilder.updating(recordType, properties()).with(property, value);
        }

    }
}
