package com.neftxx.interpreter.ast.type;

public class UndefinedType extends AritType {
    public static final UndefinedType UNDEFINED = new UndefinedType("undefined", -10);

    private UndefinedType(String name, int priority) {
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
