package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.interpreter.ast.type.TypeFacade;
import org.jetbrains.annotations.NotNull;

public abstract class StructureNode {
    public AritType baseType;

    public abstract int size();
    public abstract StructureNode copy();
    public abstract Object getValue(int position, int[] indexes) throws IndexOutOfBoundsException;
    public abstract void setValue(int position, @NotNull int[] indexes, Object value) throws IndexOutOfBoundsException;

    protected static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();
}
