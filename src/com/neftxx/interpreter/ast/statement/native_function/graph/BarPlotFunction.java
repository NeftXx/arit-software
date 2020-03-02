package com.neftxx.interpreter.ast.statement.native_function.graph;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class BarPlotFunction extends NativeFunction {
    private BarPlotFunction() {
        super("barplot");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static BarPlotFunction getInstance() {
        return INSTANCE;
    }

    private static final BarPlotFunction INSTANCE = new BarPlotFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
