package com.neftxx.interpreter.ast.expression;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;

public class Literal extends Expression {
    public Literal(NodeInfo info, AritType type, Object value) {
        super(info);
        this.type = type;
        this.value = value;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        return new AritVector(this.type, this.value);
    }
}
