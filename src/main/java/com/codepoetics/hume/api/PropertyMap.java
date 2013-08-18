package com.codepoetics.hume.api;

public interface PropertyMap<R extends Record<R>> {
    public <T> T get(Property<R, T> property);
    public <T> void set(Property<R, T> property, T value);
    public PropertyMap<R> immutableCopy();
    public PropertyMap<R> mutableCopy();
}
