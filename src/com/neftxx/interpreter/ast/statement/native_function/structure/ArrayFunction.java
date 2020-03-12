package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.*;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ArrayFunction extends NativeFunction {
    private ArrayFunction() {
        super("array");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        this.type = TYPE_FACADE.getUndefinedType();
        int argumentsSize = arguments.size();
        if (argumentsSize > 1) {
            Expression expression = arguments.get(0);
            Expression dimensionsExp = arguments.get(1);
            Object resultExp = expression.interpret(aritLanguage, scope);
            Object resultDimensionExp = dimensionsExp.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isVectorType(dimensionsExp.type)) {
                AritVector aritVectorDimension = (AritVector) resultDimensionExp;
                int[] indexes;
                int numberDims = aritVectorDimension.size();
                if (TYPE_FACADE.isIntegerType(aritVectorDimension.baseType)) {
                    int i;
                    indexes = new int[numberDims];
                    for (i = 0; i < numberDims; i++) {
                        indexes[i] = (int) aritVectorDimension.getDataNodes().get(i).value;
                    }
                } else {
                    aritLanguage.addSemanticError("Error : en la funcion array el segundo argumento debe ser " +
                            " de tipo integer.", info);
                    return null;
                }
                ArrayList<DataNode> dataNodes;
                if (TYPE_FACADE.isVectorType(expression.type)) {
                    AritVector temp = ((AritVector) resultExp).copy();
                    dataNodes = temp.getDataNodes();
                } else if (TYPE_FACADE.isListType(expression.type)) {
                    AritList temp = ((AritList) resultExp).copy();
                    dataNodes = temp.getDataNodes();
                } else if (TYPE_FACADE.isMatrixType(expression.type)) {
                    AritMatrix temp = ((AritMatrix) resultExp).copy();
                    dataNodes = (ArrayList<DataNode>) Arrays.asList(temp.getDataNodes());
                } else if (TYPE_FACADE.isArrayType(expression.type)) {
                    AritArray temp = ((AritArray) resultExp).copy();
                    dataNodes = temp.getDataNodes();
                } else {
                    aritLanguage.addSemanticError("Error : en la funcion array el primer parametro debe " +
                            "ser una estructura.", info);
                    return null;
                }
                int i, size = dataNodes.size();
                AritType currentType;
                if (size < 1) {
                    aritLanguage.addSemanticError("Error : en la funcion array, no existen datos.", info);
                    return null;
                } else {
                    currentType = dataNodes.get(0).type;
                }
                for (i = 1; i < size; i++) {
                    if (currentType != dataNodes.get(i).type) {
                        aritLanguage.addSemanticError("Error : en la funcion array los datos pasados en el " +
                                "argumento 1 deben ser del mismo tipo", info);
                        return null;
                    }
                }
                this.type = TYPE_FACADE.getArrayType();
                return AritArray.createNewArray(dataNodes, numberDims, indexes, currentType);
            } else {
                aritLanguage.addSemanticError("Error : en la funcion array el segundo parametro debe ser un " +
                        "vector de tipo integer.", info);
            }
        } else {
            aritLanguage.addSemanticError("Error : no se encontró la funcion array con la cantidad de parametros " +
                    argumentsSize + ".", info);
        }
        return null;
    }

    public static ArrayFunction getInstance() {
        return INSTANCE;
    }

    private static final ArrayFunction INSTANCE = new ArrayFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
