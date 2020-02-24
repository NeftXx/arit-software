package com.neftxx.interpreter.ast.type;

public class UndefinedType extends AritType {
    public static final UndefinedType UNDEFINED = new UndefinedType("undefined", -1);
    public static final UndefinedType DEFAULT = new UndefinedType("default", -1);

    private UndefinedType(String name, int priority) {
        super(name, priority);
    }

    @Override
    public String toString() {
        return name;
    }
}
