package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.interpreter.ast.type.AritType;

import java.util.ArrayList;

public class ConcatenateFunction extends NativeFunction {
    private ConcatenateFunction() { super("c"); }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        this.type = TYPE_FACADE.getUndefinedType();
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        AritType typeTemp = this.type;
        Object value;
        // TODO: Falta agregar validaciones para las matrices y arreglos (se deben convertir a vectores)
        for (Expression argument: arguments) {
            value = argument.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(argument.type)) {
                this.type = argument.type;
                aritLanguage.addSemanticError("Error al calcular el valor del parametro de la posición `" +
                        arguments.indexOf(argument) + "` de la función `c()`", argument.info);
                return null;
            } else if (TYPE_FACADE.isVectorType(argument.type)) {
                AritVector vector = (AritVector) value;
                if (vector.type.priority > typeTemp.priority) typeTemp = vector.type;
                dataNodes.addAll(vector.getDataNodes());
            } else if (TYPE_FACADE.isListType(argument.type)) {
                typeTemp = TYPE_FACADE.getListType();
            } else {
                if (argument.type.priority > typeTemp.priority) typeTemp = argument.type;
                dataNodes.add(new DataNode(typeTemp, value));
            }
        }
        if (TYPE_FACADE.isListType(typeTemp)) {
            this.type = TYPE_FACADE.getListType();
            return null;
        }
        this.type = TYPE_FACADE.getVectorType();
        for (DataNode dataNode: dataNodes) {
            if (typeTemp.priority > dataNode.type.priority) {
                Object cast = TYPE_FACADE.castValue(dataNode.type, typeTemp, dataNode.value);
                dataNode.changeValues(typeTemp, cast);
            }
        }
        return new AritVector(typeTemp, dataNodes);
    }

    public static ConcatenateFunction getInstance() {
        return INSTANCE;
    }

    private static final ConcatenateFunction INSTANCE = new ConcatenateFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
