package com.codepoetics.hume;

import java.util.Map;

import com.codepoetics.hume.api.Property;
import com.codepoetics.hume.api.PropertyMap;
import com.codepoetics.hume.api.Record;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public final class PropertyMaps {
    
    private PropertyMaps() { }
    
    public static <R extends Record<R>> PropertyMap<R> newPropertyMap() {
        return new SimplePropertyMap<R>();
    }

    private static final class SimplePropertyMap<R extends Record<R>> implements
            PropertyMap<R> {
        private final Map<String, Object> properties;

        private SimplePropertyMap() {
            this.properties = Maps.newHashMap();
        }
        
        private SimplePropertyMap(Map<String, Object> properties) {
            this.properties = properties;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Property<R, T> property) {
            return (T) properties.get(property.name());
        }

        @Override
        public <T> void set(Property<R, T> property, T value) {
            properties.put(property.name(), value);
        }

        @Override
        public PropertyMap<R> immutableCopy() {
            return new SimplePropertyMap<R>(ImmutableMap.copyOf(properties));
        }
        
        @Override
        public PropertyMap<R> mutableCopy() {
            return new SimplePropertyMap<R>(Maps.newHashMap(properties));
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object other) {
            if (this == other) { return true; }
            if (other == null || !(other instanceof SimplePropertyMap)) { return false; }
            return properties.equals(((SimplePropertyMap<R>) other).properties);
        }
        
        @Override
        public int hashCode() {
            return properties.hashCode();
        }
    }
}
