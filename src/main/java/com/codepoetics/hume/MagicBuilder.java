package com.codepoetics.hume;

import com.codepoetics.hume.api.Builder;
import com.codepoetics.hume.api.Property;
import com.codepoetics.hume.api.PropertyMap;
import com.codepoetics.hume.api.Record;

class MagicBuilder<R extends Record<R>> implements Builder<R> {

    public static <R extends Record<R>> Builder<R> ofType(Class<R> recordType) {
        PropertyMap<R> properties = PropertyMaps.newPropertyMap();
        return updating(recordType, properties);
    }
    
    @SuppressWarnings("unchecked")
    public static <R extends Record<R>> Builder<R> updating(R record) {
        Class<R> recordType = (Class<R>) record.getClass();
        PropertyMap<R> properties = record.properties();
        return updating(recordType, properties);
    }
    
    public static <R extends Record<R>> Builder<R> updating(Class<R> recordType, PropertyMap<R> properties) {
        return new MagicBuilder<R>(recordType, properties);
    }
    
    private final Class<R> recordType;
    private PropertyMap<R> properties;
    
    private MagicBuilder(Class<R> recordType, PropertyMap<R> properties) {
        this.recordType = recordType;
        this.properties = properties.mutableCopy();
    }
    
    @Override
    public <T> Builder<R> with(Property<R, T> property, T value) {
        properties.set(property, value);
        return this;
    }

    @Override
    public R get() {
        return MagicRecord.ofType(recordType).with(properties);
    }

}
