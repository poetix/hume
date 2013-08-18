package com.codepoetics.hume;

import com.codepoetics.hume.api.Record;
import com.codepoetics.hume.api.Builder;

public final class Magic {

    private Magic() { }
    
    public static <R extends Record<R>> Builder<R> builderFor(Class<R> recordType) {
        return MagicBuilder.ofType(recordType);
    }
    
    public static <R extends Record<R>> R lensesOf(Class<R> recordType) {
        return MagicLens.forType(recordType);
    }
}
