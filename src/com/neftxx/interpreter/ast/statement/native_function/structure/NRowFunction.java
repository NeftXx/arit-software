package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.interpreter.ast.statement.native_function.RemoveFunction;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class NRowFunction extends NativeFunction {
    private NRowFunction() {
        super("nRow");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        return null;
    }

    public static NRowFunction getInstance() {
        return INSTANCE;
    }

    private static final NRowFunction INSTANCE = new NRowFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
