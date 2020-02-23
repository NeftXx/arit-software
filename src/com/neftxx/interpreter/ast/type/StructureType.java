package com.neftxx.interpreter.ast.type;

public class StructureType extends AritType {
    public static final StructureType LIST = new StructureType("list", 50);
    public static final StructureType VECTOR = new StructureType("vector", 0);
    public static final StructureType ARRAY = new StructureType("array", 0);
    public static final StructureType MATRIX = new StructureType("matrix", 0);

    protected StructureType(String name, int priority) {
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
