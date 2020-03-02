package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class StringLengthFunction extends NativeFunction {
    private StringLengthFunction() {
        super("StringLength");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static StringLengthFunction getInstance() {
        return INSTANCE;
    }

    private static final StringLengthFunction INSTANCE = new StringLengthFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
