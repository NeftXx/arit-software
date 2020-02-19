package com.neftxx.interpreter.ast.expression;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.function.Function;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class FunctionCall extends Expression {
    public final String id;
    public final ArrayList<Expression> arguments;

    public FunctionCall(NodeInfo info, String id, ArrayList<Expression> arguments) {
        super(info);
        this.id = id;
        this.arguments = arguments;
    }

    public FunctionCall(NodeInfo info, String id) {
        this(info, id, new ArrayList<>());
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        NativeFunction nativeFunction = aritLanguage.globalScope.getNativeFunction(this.id);
        if (nativeFunction != null) {
            nativeFunction.interpret(aritLanguage, this.arguments);
        } else {
            int numberOfArguments = arguments.size();
            Function function = aritLanguage.globalScope.getMethod(this.id);
            if (function.numberOfParameters() == Integer.MAX_VALUE) {

            }

            if (numberOfArguments > 0) {
                Object[] values = new Object[numberOfArguments];
                int i = 0;
                Expression expression;
                for (; i < numberOfArguments; i++) {
                    expression = this.arguments.get(i);
                    values[i] = expression.interpret(aritLanguage, scope);
                    if (TYPE_FACADE.isUndefinedType(expression.type)) {
                        aritLanguage.addSemanticError("Error al evaluar el parametro " + (i + 1) +
                                " en la llamada a la funcion" + this.id, this.info);
                        this.type = TYPE_FACADE.getUndefinedType();
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
