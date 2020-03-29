package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RoundFunction extends NativeFunction {
    private RoundFunction() {
        super("Round");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        int argumentsSize = arguments.size();
        this.type = TYPE_FACADE.getUndefinedType();
        if (argumentsSize == 1) {
            Object valueResult = arguments.get(0).interpret(aritLanguage, scope);
            if (valueResult instanceof AritVector) {
                AritVector vector = (AritVector) valueResult;
                if (isNumeric(vector)) {
                    this.type = TYPE_FACADE.getVectorType();
                    return new AritVector(new DataNode(TYPE_FACADE.getIntegerType(),
                            toInt(vector.getDataNodes().get(0).value)));
                } else {
                    aritLanguage.addSemanticError("Error : se esperaba un vector de tipo numerico en la función `round()`.", info);
                }
            } else  {
                aritLanguage.addSemanticError("Error : se esperaba un vector de tipo numerico en la función `round()`.", info);
            }
        } else {
            aritLanguage.addSemanticError("Error : no se encontró la funcion Round con la cantidad de parametros `" +
                    argumentsSize + "`.", info);
        }
        return null;
    }

    private boolean isNumeric(@NotNull AritVector vector) {
        return TYPE_FACADE.isIntegerType(vector.baseType) || TYPE_FACADE.isNumericType(vector.baseType);
    }

    @NotNull
    @Contract(pure = true)
    private Integer toInt(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Double) {
            Double temp = (Double) value;
            long round = Math.round(temp);
            return (int) round;
        }
        return 0;
    }

    public static RoundFunction getInstance() {
        return INSTANCE;
    }

    private static final RoundFunction INSTANCE = new RoundFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
