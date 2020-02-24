package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;

import java.util.ArrayList;

public class ArrayFunction extends NativeFunction {
    private ArrayFunction() {
        super("array");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static ArrayFunction getInstance() {
        return INSTANCE;
    }

    private static final ArrayFunction INSTANCE = new ArrayFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
