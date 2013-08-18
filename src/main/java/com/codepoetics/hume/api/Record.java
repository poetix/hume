package com.codepoetics.hume.api;

public interface Record<R extends Record<R>> {

    <T> T get(Property<R, T> property);
    PropertyMap<R> properties();
    <T> Builder<R> with(Property<R, T> property, T value);
    
}
