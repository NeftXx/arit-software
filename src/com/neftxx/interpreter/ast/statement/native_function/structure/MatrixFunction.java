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

    // TODO: terminar las matrices, estaba calculando las expresiones
    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        this.type = TYPE_FACADE.getUndefinedType();
        int argumentsSize = arguments.size();
        if (argumentsSize > 2) {
            Expression vectorExp = arguments.get(0);
            Expression rowsExp = arguments.get(1);
            Expression colExp = arguments.get(2);

        } else {
            aritLanguage.addSemanticError("Error : no se encontró la funcion matrix con la cantidad de parametros `" +
                    argumentsSize + "`.", info);
        }
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
