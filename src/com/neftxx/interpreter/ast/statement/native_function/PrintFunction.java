package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PrintFunction extends NativeFunction {
    private PrintFunction() { super("print"); }

    @Override
    public Object interpret(AritLanguage aritLanguage, @NotNull ArrayList<Expression> arguments, Scope scope) {
        int numberOfArguments = arguments.size();
        if (numberOfArguments > 0) {
            Expression expression = arguments.get(0);
            Object result = expression.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(expression.type)) {
                StringBuilder cad = new StringBuilder();
                for (Expression argument: arguments) cad.append(argument).append(" ");
                aritLanguage.addSemanticError("Error en print( " + cad + ") : al calcular el valor del parametro de la posición `1`.",
                        expression.info);
                return null;
            }
            aritLanguage.printOnConsole(">");
            aritLanguage.printOnConsole(result != null ? result.toString() : "NULL");
        } else {
            aritLanguage.printOnConsole("\n");
        }
        return null;
    }

    public static PrintFunction getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private static final PrintFunction INSTANCE = new PrintFunction();
}
