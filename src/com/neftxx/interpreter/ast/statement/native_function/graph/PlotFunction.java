package com.neftxx.interpreter.ast.statement.native_function.graph;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class PlotFunction extends NativeFunction {
    private PlotFunction() {
        super("plot");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static PlotFunction getInstance() {
        return INSTANCE;
    }

    private static final PlotFunction INSTANCE = new PlotFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
