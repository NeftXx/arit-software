package com.neftxx.interpreter.ast.statement.native_function.graph;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class HistFunction extends NativeFunction {
    private HistFunction() {
        super("hist");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static HistFunction getInstance() {
        return INSTANCE;
    }

    private static final HistFunction INSTANCE = new HistFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
