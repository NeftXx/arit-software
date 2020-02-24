package com.neftxx.interpreter.ast.statement.native_function.statistic;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.interpreter.ast.statement.native_function.structure.ArrayFunction;

import java.util.ArrayList;

public class MedianFunction extends NativeFunction {
    private MedianFunction() {
        super("median");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static MedianFunction getInstance() {
        return INSTANCE;
    }

    private static final MedianFunction INSTANCE = new MedianFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
