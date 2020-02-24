package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;

import java.util.ArrayList;

public class TypeofFunction extends NativeFunction {
    private TypeofFunction() {
        super("typeof");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static TypeofFunction getInstance() {
        return INSTANCE;
    }

    private static final TypeofFunction INSTANCE = new TypeofFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
