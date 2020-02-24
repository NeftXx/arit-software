package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;

import java.util.ArrayList;

public class ToLowerCaseFunction extends NativeFunction {
    private ToLowerCaseFunction() {
        super("toLowerCase");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static ToLowerCaseFunction getInstance() {
        return INSTANCE;
    }

    private static final ToLowerCaseFunction INSTANCE = new ToLowerCaseFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
