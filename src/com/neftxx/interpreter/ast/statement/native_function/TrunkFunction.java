package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;

import java.util.ArrayList;

public class TrunkFunction extends NativeFunction {
    private TrunkFunction() {
        super("trunk");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static TrunkFunction getInstance() {
        return INSTANCE;
    }

    private static final TrunkFunction INSTANCE = new TrunkFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
