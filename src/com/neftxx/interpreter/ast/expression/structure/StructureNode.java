package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;

public abstract class StructureNode {
    public AritType type;

    public abstract int size();
    public abstract StructureNode copy();
}
