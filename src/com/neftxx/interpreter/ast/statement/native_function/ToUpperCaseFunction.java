package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class ToUpperCaseFunction extends NativeFunction {
    private ToUpperCaseFunction() {
        super("toUpperCase");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static ToUpperCaseFunction getInstance() {
        return INSTANCE;
    }

    private static final ToUpperCaseFunction INSTANCE = new ToUpperCaseFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
