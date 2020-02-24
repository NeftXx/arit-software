package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;

import java.util.ArrayList;

public class LengthFunction extends NativeFunction {
    private LengthFunction() {
        super("length");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static LengthFunction getInstance() {
        return INSTANCE;
    }

    private static final LengthFunction INSTANCE = new LengthFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
