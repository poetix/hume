package com.codepoetics.hume;

import com.codepoetics.hume.api.Property;
import com.codepoetics.hume.api.Record;
import com.codepoetics.spinoza.api.ComposableLens;
import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.api.PathDescriber;

class BoundProperty<R extends Record<R>, T> implements Property<R, T> {

    private final Property<R, T> unboundProperty;
    private final Record<R> boundTarget;
    
    public static <R extends Record<R>, T> Property<R, T> bind(Property<R, T> unboundProperty, Record<R> boundTarget) {
        return new BoundProperty<R, T>(unboundProperty, boundTarget);
    }
    
    private BoundProperty(Property<R, T> unboundProperty, Record<R> boundTarget) {
        this.unboundProperty = unboundProperty;
        this.boundTarget = boundTarget;
    }
    
    @Override
    public String name() {
        return unboundProperty.name();
    }
    
    @Override
    public Class<R> targetClass() {
        return unboundProperty.targetClass();
    }
    
    @Override
    public <T2> ComposableLens<R, T2> with(Lens<T, T2> other) {
        return unboundProperty.with(other);
    }

    @Override
    public <T2> ComposableLens<R, T2> with(PathDescribingLens<T, T2> other) {
        return unboundProperty.with(other);
    }

    @Override
    public T get(R target) {
        return unboundProperty.get(target);
    }

    @Override
    public R update(R target, T newValue) {
        return unboundProperty.update(target, newValue);
    }

    @Override
    public void describeTo(PathDescriber describer) {
        unboundProperty.describeTo(describer);
    }

    @Override
    public T get() {
        return boundTarget.get(unboundProperty);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || !(obj instanceof BoundProperty)) { return false; }
        BoundProperty<?, ?> other = (BoundProperty<?, ?>) obj;
        return boundTarget.equals(other.boundTarget) && unboundProperty.equals(other.unboundProperty);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + boundTarget.hashCode();
        return prime * result + unboundProperty.hashCode();
    }

}
