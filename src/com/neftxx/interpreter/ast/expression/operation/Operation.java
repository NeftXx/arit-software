package com.neftxx.interpreter.ast.expression.operation;

import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.util.NodeInfo;

public abstract class Operation extends Expression {
    public Expression expLeft;
    public Expression expRight;

    public Operation(NodeInfo info, Expression expLeft, Expression expRight) {
        super(info);
        this.expLeft = expLeft;
        this.expRight = expRight;
    }
}
