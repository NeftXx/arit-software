package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class RoundFunction extends NativeFunction {
    private RoundFunction() {
        super("Round");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static RoundFunction getInstance() {
        return INSTANCE;
    }

    private static final RoundFunction INSTANCE = new RoundFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
