package com.neftxx.interpreter.ast.expression.function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

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
        this(info, id, null);
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        NativeFunction nativeFunction = aritLanguage.globalScope.getNativeFunction(this.id);
        if (nativeFunction != null) {
            this.value = nativeFunction.interpret(aritLanguage, this.arguments, scope);
            this.type = nativeFunction.type;
        } else {
//            int numberOfArguments = this.arguments != null ? arguments.size() : 0;
//            Function function = aritLanguage.globalScope.getMethod(this.id);
//            if (numberOfArguments > 0) {
//                Object[] values = new Object[numberOfArguments];
//                int i = 0;
//                Expression expression;
//                for (; i < numberOfArguments; i++) {
//                    expression = this.arguments.get(i);
//                    values[i] = expression.interpret(aritLanguage, scope);
//                    if (TYPE_FACADE.isUndefinedType(expression.type)) {
//                        aritLanguage.addSemanticError("Error al evaluar el parametro " + (i + 1) +
//                                " en la llamada a la funcion" + this.id, this.info);
//                        this.type = TYPE_FACADE.getUndefinedType();
//                        return null;
//                    }
//                }
//            }
        }
        return this.value;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
    }

    @Override
    public String toString() {
        StringBuilder cad = new StringBuilder(this.id + "(");
        for (Expression argument: this.arguments) {
            cad.append(argument);
        }
        return cad + ")";
    }
}
