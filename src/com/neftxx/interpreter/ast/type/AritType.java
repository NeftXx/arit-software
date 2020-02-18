package com.neftxx.interpreter.ast.type;

public abstract class AritType {
    public final int priority;
    protected final String name;

    protected AritType(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isSame(AritType other);

    @Override
    public String toString() {
        return name;
    }
}
