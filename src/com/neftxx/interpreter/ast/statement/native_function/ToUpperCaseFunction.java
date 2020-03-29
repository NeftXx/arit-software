package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class ToUpperCaseFunction extends NativeFunction {
    private ToUpperCaseFunction() {
        super("toUpperCase");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        int argumentsSize = arguments.size();
        this.type = TYPE_FACADE.getUndefinedType();
        if (argumentsSize == 1) {
            Object valueResult = arguments.get(0).interpret(aritLanguage, scope);
            if (valueResult instanceof AritVector) {
                AritVector vector = (AritVector) valueResult;
                if (TYPE_FACADE.isStringType(vector.baseType)) {
                    this.type = TYPE_FACADE.getVectorType();
                    String _string = toString(vector.getDataNodes().get(0).value);
                    return new AritVector(new DataNode(TYPE_FACADE.getStringType(), _string.toUpperCase()));
                } else {
                    aritLanguage.addSemanticError("Error : se esperaba un vector de tipo String en la función `toUpperCase()`.", info);
                }
            } else {
                aritLanguage.addSemanticError("Error : se esperaba un vector de tipo String en la función `toUpperCase()`.", info);
            }
        } else {
            aritLanguage.addSemanticError("Error : no se encontró la función `toUpperCase()` con la cantidad de parametros `" +
                    argumentsSize + "`.", info);
        }
        return null;
    }

    private String toString(Object value) {
        return value != null ? value.toString() : "NULL";
    }

    public static ToUpperCaseFunction getInstance() {
        return INSTANCE;
    }

    private static final ToUpperCaseFunction INSTANCE = new ToUpperCaseFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
