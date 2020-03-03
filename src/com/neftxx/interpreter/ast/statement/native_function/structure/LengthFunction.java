package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritList;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LengthFunction extends NativeFunction {
    private LengthFunction() {
        super("length");
        this.type = TYPE_FACADE.getVectorType();
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, @NotNull ArrayList<Expression> arguments, Scope scope) {
        int argumentsSize = arguments.size();
        int size = 0;
        if (argumentsSize > 0) {
            Expression expression = arguments.get(0);
            Object valueResult = expression.interpret(aritLanguage, scope);
            if (valueResult instanceof AritVector) {
                size = ((AritVector) valueResult).size();
            } else if(valueResult instanceof AritList) {
                size = ((AritList) valueResult).size();
            } else {
                aritLanguage.addSemanticError("Error : no se esperaba este tipo `" +
                        expression.type + "` en el parametro `1`.", info);
            }
        } else {
            aritLanguage.addSemanticError("Error : no se encontró la funcion length con la cantidad de parametros `" +
                    argumentsSize + "`.", info);
        }
        return new AritVector(new DataNode(TYPE_FACADE.getIntegerType(), size));
    }

    public static LengthFunction getInstance() {
        return INSTANCE;
    }

    private static final LengthFunction INSTANCE = new LengthFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
