package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class NColFunction extends NativeFunction {
    private NColFunction() {
        super("nCol");
        this.type = TYPE_FACADE.getVectorType();
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        int argumentsSize = arguments.size();
        if (argumentsSize == 1) {
            Object valueResult = arguments.get(0).interpret(aritLanguage, scope);
            if (valueResult instanceof AritMatrix) {
                return new AritVector(new DataNode(TYPE_FACADE.getIntegerType(), ((AritMatrix) valueResult).columns));
            } else {
                // TODO: AGREGAR ERROR
            }
        } else {
            aritLanguage.addSemanticError("Error : no se encontró la funcion nCol con la cantidad de parametros `" +
                    argumentsSize + "`.", info);
        }
        return null;
    }

    public static NColFunction getInstance() {
        return INSTANCE;
    }

    private static final NColFunction INSTANCE = new NColFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
