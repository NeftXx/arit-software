package com.neftxx.interpreter.ast.type;

public class BaseType extends AritType {
    public static final BaseType BOOLEAN = new BaseType("boolean", 10);
    public static final BaseType INTEGER = new BaseType("integer", 20);
    public static final BaseType NUMERIC = new BaseType("numeric", 30);
    public static final BaseType STRING = new BaseType("String", 40);

    private BaseType(String name, int priority) {
        super(name, priority);
    }

    @Override
    public boolean isSame(AritType other) {
        return this == other;
    }

    @Override
    public String toString() {
        return name;
    }
}
