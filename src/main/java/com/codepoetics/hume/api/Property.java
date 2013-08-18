package com.codepoetics.hume.api;

import com.codepoetics.spinoza.api.ComposableLens;

public interface Property<R extends Record<R>, T> extends ComposableLens<R, T>, BoundLens<R, T> {
    String name();
    Class<R> targetClass();
}
