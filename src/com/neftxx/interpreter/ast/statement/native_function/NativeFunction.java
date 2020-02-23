package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.interpreter.ast.type.TypeFacade;

import java.util.ArrayList;

public abstract class NativeFunction {
    public final String name;
    public AritType type;

    public NativeFunction(String name) {
        this.name = name;
        this.type = TYPE_FACADE.getUndefinedType();
    }
    public abstract Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope);
    protected static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();

    @Override
    public String toString() {
        return "NativeFunction " + name;
    }
}
