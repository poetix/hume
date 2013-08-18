package com.codepoetics.hume;

import com.codepoetics.hume.api.Property;
import com.codepoetics.hume.api.Record;
import com.codepoetics.spinoza.Compose;
import com.codepoetics.spinoza.api.ComposableLens;
import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.api.PathDescriber;

public class UnboundProperty<R extends Record<R>, T> implements Property<R, T> {

    private final String name;
    private final Class<R> targetClass;
    
    public UnboundProperty(String name, Class<R> targetClass) {
        this.name = name;
        this.targetClass = targetClass;
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public Class<R> targetClass() {
        return targetClass;
    }

    @Override
    public <T2> ComposableLens<R, T2> with(Lens<T, T2> other) {
        return Compose.theLens(this).with(other);
    }

    @Override
    public <T2> ComposableLens<R, T2> with(PathDescribingLens<T, T2> other) {
        return Compose.theLens(this).with(other);
    }

    @Override
    public T get(R target) {
        return target.get(this);
    }

    @Override
    public R update(R target, T newValue) {
        return target.with(this, newValue).get();
    }

    @Override
    public void describeTo(PathDescriber describer) {
        describer.path(name);
    }

    @Override
    public T get() {
        throw new UnsupportedOperationException("Property is not bound to any value");
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (other == null || !(other instanceof Property)) {
            return false;
        }
        Property<?, ?> otherProperty = (Property<?, ?>) other;
        return name.equals(otherProperty.name()) && targetClass.equals(otherProperty.targetClass());
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return prime * result + targetClass.hashCode();
    }

}
