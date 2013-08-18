package com.codepoetics.hume.api;

import com.google.common.base.Supplier;

public interface Builder<P extends Record<P>> extends Supplier<P> {
    <T> Builder<P> with(Property<P, T> property, T value);
}
