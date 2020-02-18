package com.neftxx.interpreter.ast.type;

public class EmptyType extends AritType {
    public static final EmptyType NULL = new EmptyType("null", 0);
    public static final EmptyType UNDEFINED = new EmptyType("undefined", -10);

    private EmptyType(String name, int priority) {
        super(name, priority);
    }

    @Override
    public boolean isSame(AritType other) {
        return other == this;
    }

    @Override
    public String toString() {
        return name;
    }
}
