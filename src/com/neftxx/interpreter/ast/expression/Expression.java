package com.neftxx.interpreter.ast.expression;

import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;

public abstract class Expression extends AstNode {
    public AritType type;
    public Object value;

    public Expression(NodeInfo info) {
        super(info);
        this.type = TYPE_FACADE.getUndefinedType();
        this.value = null;
    }
}
