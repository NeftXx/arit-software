package com.neftxx.interpreter.ast.statement.native_function.graph;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;

import java.util.ArrayList;

public class PieFunction extends NativeFunction {
    private PieFunction() {
        super("pie");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static PieFunction getInstance() {
        return INSTANCE;
    }

    private static final PieFunction INSTANCE = new PieFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
