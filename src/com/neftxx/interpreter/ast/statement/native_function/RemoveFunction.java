package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;

import java.util.ArrayList;

public class RemoveFunction extends NativeFunction {
    private RemoveFunction() {
        super("Remove");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static RemoveFunction getInstance() {
        return INSTANCE;
    }

    private static final RemoveFunction INSTANCE = new RemoveFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
