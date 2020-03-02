package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class MatrixFunction extends NativeFunction {
    private MatrixFunction() {
        super("matrix");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static MatrixFunction getInstance() {
        return INSTANCE;
    }

    private static final MatrixFunction INSTANCE = new MatrixFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
