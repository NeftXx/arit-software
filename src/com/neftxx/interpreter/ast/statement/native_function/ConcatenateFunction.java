package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;

import java.util.ArrayList;

public class ConcatenateFunction extends NativeFunction {
    private ConcatenateFunction() { super("c"); }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static ConcatenateFunction getInstance() {
        return INSTANCE;
    }

    private static final ConcatenateFunction INSTANCE = new ConcatenateFunction();
}
