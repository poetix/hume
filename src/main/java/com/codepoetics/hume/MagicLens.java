package com.codepoetics.hume;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import com.codepoetics.hume.api.Property;
import com.codepoetics.hume.api.Record;

final class MagicLens {

    private MagicLens() {
    }

    @SuppressWarnings("unchecked")
    public static <R extends Record<R>> R forType(
            final Class<R> recordType) {
                return (R) Proxy.newProxyInstance(recordType.getClassLoader(),
                        new Class<?>[] { recordType },
                        new UnboundProxyInvocationHandler<R>(recordType));
    }

    private static class UnboundProxyInvocationHandler<R extends Record<R>>
            implements InvocationHandler {

        private Map<Method, Property<R, ?>> methodMap;

        public UnboundProxyInvocationHandler(Class<R> recordType) {
            methodMap = PropertyDictionary.unboundPropertiesForType(recordType);
        }

        @Override
        public Object invoke(Object arg0, Method arg1, Object[] arg2)
                throws Throwable {
            if (methodMap.containsKey(arg1)) {
                return methodMap.get(arg1);
            }
            return arg1.invoke(this, arg2);
        }
    }
}
