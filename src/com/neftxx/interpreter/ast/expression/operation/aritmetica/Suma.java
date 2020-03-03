package com.neftxx.interpreter.ast.expression.operation.aritmetica;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.operation.Operation;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Suma extends Operation {
    public Suma(NodeInfo info, Expression expLeft, Expression expRight) {
        super(info, expLeft, expRight);
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }
}
