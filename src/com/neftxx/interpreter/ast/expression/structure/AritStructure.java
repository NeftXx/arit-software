package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.TypeFacade;

public abstract class AritStructure {
    public abstract AritStructure copy();
    protected static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();
}
