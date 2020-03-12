package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.interpreter.ast.type.TypeFacade;

public abstract class AritStructure {
    public AritType baseType;
    public abstract int size();
    public abstract AritStructure copy();
    protected static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();
}
