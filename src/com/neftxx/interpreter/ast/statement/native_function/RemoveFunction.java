package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class RemoveFunction extends NativeFunction {
    private RemoveFunction() {
        super("Remove");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        int argumentsSize = arguments.size();
        this.type = TYPE_FACADE.getUndefinedType();
        if (argumentsSize == 2) {
            Expression expString = arguments.get(0);
            Expression expRemove = arguments.get(1);
            Object resultString = expString.interpret(aritLanguage, scope);
            Object resultRemove = expRemove.interpret(aritLanguage, scope);
            if (resultString instanceof AritVector && resultRemove instanceof AritVector) {
                AritVector vectorString = (AritVector) resultString;
                AritVector vectorRemove = (AritVector) resultRemove;
                if (TYPE_FACADE.isStringType(vectorString.baseType) && vectorString.size() == 1 &&
                        TYPE_FACADE.isStringType(vectorRemove.baseType) && vectorRemove.size() == 1) {
                    String _string = toString(vectorString.getDataNodes().get(0).value);
                    String _remove = toString(vectorRemove.getDataNodes().get(0).value);
                    String result = _string.replaceAll(_remove, "");
                    this.type = TYPE_FACADE.getVectorType();
                    return new AritVector(new DataNode(TYPE_FACADE.getStringType(), result));
                } else {
                    aritLanguage.addSemanticError("Error : se esperaba un vector de tipo numerico.", info);
                }
            } else {
                aritLanguage.addSemanticError("Error : se esperaba un vector de tipo numerico.", info);
            }
        } else {
            aritLanguage.addSemanticError("Error : no se encontró la funcion Remove con la cantidad de parametros `" +
                    argumentsSize + "`.", info);
        }
        return null;
    }

    private String toString(Object value) {
        return value != null ? value.toString() : "NULL";
    }

    public static RemoveFunction getInstance() {
        return INSTANCE;
    }

    private static final RemoveFunction INSTANCE = new RemoveFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
