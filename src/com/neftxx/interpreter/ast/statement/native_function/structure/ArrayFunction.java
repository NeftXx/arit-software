package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.*;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

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
            if (resultDimensionExp instanceof AritVector) {
                AritVector aritVectorDimension = (AritVector) resultDimensionExp;
                int[] indexes;
                int numberDims = aritVectorDimension.size();
                if (TYPE_FACADE.isIntegerType(aritVectorDimension.baseType)) {
                    int j = 0;
                    int i = numberDims - 1;
                    indexes = new int[numberDims];
                    Object value;
                    for (; i >= 0; i--, j++) {
                        value = aritVectorDimension.getDataNodes().get(j).value;
                        indexes[i] = value instanceof Number ? ((Number) value).intValue() : 0;
                    }
                } else {
                    aritLanguage.addSemanticError("Error : en la funcion array el segundo argumento debe ser " +
                            " de tipo integer.", info);
                    return null;
                }
                boolean itsFixData = false;
                ArrayList<DataNode> dataNodes;
                AritType principalType;
                AritType auxType;
                if (resultExp instanceof AritVector) {
                    AritVector temp = ((AritVector) resultExp).copy();
                    dataNodes = temp.getDataNodes();
                    principalType = TYPE_FACADE.getVectorType();
                    auxType = temp.baseType;
                } else if (resultExp instanceof AritList) {
                    AritList temp = ((AritList) resultExp).copy();
                    dataNodes = temp.getDataNodes();
                    principalType = TYPE_FACADE.getListType();
                    auxType = principalType;
                } else if (resultExp instanceof AritMatrix) {
                    AritMatrix temp = ((AritMatrix) resultExp).copy();
                    dataNodes = temp.getAuxDataNodes();
                    principalType = TYPE_FACADE.getVectorType();
                    auxType = temp.baseType;
                } else if (resultExp instanceof AritArray) {
                    AritArray temp = ((AritArray) resultExp).copy();
                    dataNodes = temp.getDataNodes();
                    principalType = temp.principalType;
                    auxType = temp.auxType;
                    itsFixData = true;
                } else {
                    aritLanguage.addSemanticError("Error : en la funcion array el primer parametro debe " +
                            "ser una estructura.", info);
                    return null;
                }
                int i, size = dataNodes.size();
                if (size < 1) {
                    aritLanguage.addSemanticError("Error : en la funcion array, no existen datos.", info);
                    return null;
                }
                ArrayList<DataNode> newDataNodes = new ArrayList<>();
                for (i = 0; i < size; i++) {
                    if (itsFixData) {
                        newDataNodes.add(dataNodes.get(i));
                    } else {
                        if (TYPE_FACADE.isVectorType(principalType)) {
                            newDataNodes.add(new DataNode(principalType, new AritVector(dataNodes.get(i))));
                        } else {
                            newDataNodes.add(new DataNode(principalType, new AritList(dataNodes.get(i))));
                        }
                    }
                }
                this.type = TYPE_FACADE.getArrayType();
                return AritArray.createNewArray(newDataNodes, numberDims, indexes, principalType, auxType);
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
