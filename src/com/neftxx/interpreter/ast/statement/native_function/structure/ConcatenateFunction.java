package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritList;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class ConcatenateFunction extends NativeFunction {
    private ConcatenateFunction() { super("c"); }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        this.type = TYPE_FACADE.getUndefinedType();
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        AritType typeTemp = this.type;
        Object value;
        for (Expression argument: arguments) {
            value = argument.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(argument.type) || TYPE_FACADE.isMatrixType(argument.type)
                    || TYPE_FACADE.isArrayType(argument.type)) {
                aritLanguage.addSemanticError("Error : al calcular el valor del parametro de la posición `" +
                        arguments.indexOf(argument) + "` de la función `c()`. No se permite objetos del tipo " + argument.type +
                        " en esta función.", argument.info);
                return null;
            } else if (TYPE_FACADE.isVectorType(argument.type)) {
                AritVector vector = (AritVector) value;
                if (vector.baseType.priority > typeTemp.priority) typeTemp = vector.baseType;
                if (argument.verifyCopy()) vector = vector.copy();
                dataNodes.addAll(vector.getDataNodes());
            } else if (TYPE_FACADE.isListType(argument.type)) {
                if (!TYPE_FACADE.isListType(typeTemp)) typeTemp = TYPE_FACADE.getListType();
                AritList list = (AritList) value;
                if (argument.verifyCopy()) list = list.copy();
                dataNodes.addAll(list.getDataNodes());
            } else {
                if (argument.type.priority > typeTemp.priority) typeTemp = argument.type;
                dataNodes.add(new DataNode(typeTemp, value));
            }
        }
        if (TYPE_FACADE.isListType(typeTemp)) {
            this.type = TYPE_FACADE.getListType();
            for(DataNode currentNode: dataNodes) {
                if (TYPE_FACADE.isBaseType(currentNode.type)) {
                    AritType currentType = currentNode.type;
                    Object currentValue = currentNode.value;
                    currentNode.changeValues(TYPE_FACADE.getVectorType(), new AritVector(currentType, currentValue));
                }
            }
            return new AritList(dataNodes);
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
