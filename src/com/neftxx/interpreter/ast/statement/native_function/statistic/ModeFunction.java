package com.neftxx.interpreter.ast.statement.native_function.statistic;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ModeFunction extends NativeFunction {
    public ModeFunction() {
        super("mode");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static ModeFunction getInstance() {
        return INSTANCE;
    }

    private static final ModeFunction INSTANCE = new ModeFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
