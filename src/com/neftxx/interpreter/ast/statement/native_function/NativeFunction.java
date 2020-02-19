package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.TypeFacade;

import java.util.ArrayList;

public abstract class NativeFunction {
    public final String name;
    public NativeFunction(String name) {
        this.name = name;
    }
    public abstract Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope);
    protected static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();
}
