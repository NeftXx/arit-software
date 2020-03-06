package com.neftxx.interpreter.ast.expression.operation;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Comparator extends Operation {
    public enum Operator {
        EQUAL("=="), UNEQUAL("!=");
        String op;

        Operator(String op) {
            this.op = op;
        }

        @Override
        public String toString() {
            return this.op;
        }
    }

    public Operator operator;

    public Comparator(NodeInfo info, Expression expLeft, Expression expRight, Operator operator) {
        super(info, expLeft, expRight);
        this.operator = operator;
    }

    @Override
    protected AritType getMaxType(AritType type1, AritType type2) {
        if (TYPE_FACADE.isBooleanType(type1) && TYPE_FACADE.isBooleanType(type2)) {
            return TYPE_FACADE.getBooleanType();
        }
        if (TYPE_FACADE.isStringType(type1) && TYPE_FACADE.isStringType(type2)) {
            return TYPE_FACADE.getStringType();
        }
        if (TYPE_FACADE.isIntegerType(type1) && TYPE_FACADE.isIntegerType(type2)) {
            return TYPE_FACADE.getIntegerType();
        }
        if (TYPE_FACADE.isIntegerType(type1) && TYPE_FACADE.isNumericType(type2) ||
                TYPE_FACADE.isNumericType(type1) && TYPE_FACADE.isIntegerType(type2) ||
                TYPE_FACADE.isNumericType(type1) && TYPE_FACADE.isNumericType(type2)) {
            return TYPE_FACADE.getNumericType();
        }
        return TYPE_FACADE.getUndefinedType();
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object resultLeft = this.expLeft.interpret(aritLanguage, scope);
        Object resultRight = this.expRight.interpret(aritLanguage, scope);
        if (TYPE_FACADE.isVectorType(expLeft.type) && TYPE_FACADE.isVectorType(expRight.type)) {
            if (operateVectors(aritLanguage, (AritVector) resultLeft, (AritVector) resultRight)) return this.value;
        } else if (TYPE_FACADE.isMatrixType(expLeft.type) && TYPE_FACADE.isMatrixType(expRight.type)) {
            if (operateMatrix(aritLanguage, (AritMatrix) resultLeft, (AritMatrix) resultRight)) return this.value;
        } else if (TYPE_FACADE.isMatrixType(expLeft.type) && TYPE_FACADE.isVectorType(expRight.type)) {
            if (operateMatrixVector(aritLanguage, (AritMatrix) resultLeft, (AritVector) resultRight)) return this.value;
        } else if (TYPE_FACADE.isVectorType(expLeft.type) && TYPE_FACADE.isMatrixType(expRight.type)) {
            if (operateMatrixVector(aritLanguage, (AritMatrix) resultRight, (AritVector) resultLeft)) return this.value;
        }
        else {
            aritLanguage.addSemanticError("Error en " + this + " : no se puede operar las estructuras " +
                    expLeft.type + " y " + expRight.type + ".", this.info);
        }
        this.type = TYPE_FACADE.getUndefinedType();
        return null;
    }

    private boolean operateMatrixVector(
            AritLanguage aritLanguage, AritMatrix aritMatrix, @NotNull AritVector aritVector
    ) {
        if (aritVector.size() == 1) {
            AritType typeTemp = getMaxType(aritMatrix.baseType, aritVector.baseType);
            if (TYPE_FACADE.isBooleanType(typeTemp) || TYPE_FACADE.isStringType(typeTemp) ||
                    TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp)) {
                int i, sizeMatriz = aritMatrix.size();
                DataNode[] dataNodeArrayList = new DataNode[sizeMatriz];
                AritType boolType = TYPE_FACADE.getBooleanType();
                boolean res, isNumber = TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp);
                Object currentVal, scalar = aritVector.getDataNodes().get(0).value;
                if (isNumber) scalar = toDouble(scalar);
                for (i = 0; i < sizeMatriz; i++) {
                    if (isNumber) currentVal = toDouble(aritMatrix.getDataNodes()[i].value);
                    else currentVal = aritMatrix.getDataNodes()[i].value;
                    res = (this.operator == Operator.EQUAL) == (currentVal == scalar);
                    dataNodeArrayList[i] = new DataNode(boolType, res);
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(boolType, dataNodeArrayList, aritMatrix.rows, aritMatrix.columns);
                return true;
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : No se puede comparar una matriz de " +
                        "tipo " + aritMatrix.baseType + " con un vector de tipo " + aritVector.baseType + ".", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en " + this + " : No se puede operar una matriz con" +
                    " un vector de un tamaño mayor a uno.", this.info);
        }
        return false;
    }

    private boolean operateMatrix(
            AritLanguage aritLanguage, @NotNull AritMatrix aritMatrixLeft, @NotNull AritMatrix aritMatrixRight
    ) {
        if (aritMatrixLeft.rows == aritMatrixRight.rows && aritMatrixLeft.columns == aritMatrixRight.columns) {
            AritType typeTemp = getMaxType(aritMatrixLeft.baseType, aritMatrixRight.baseType);
            if (TYPE_FACADE.isBooleanType(typeTemp) || TYPE_FACADE.isStringType(typeTemp) ||
                    TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp)) {
                int i, sizeMatrix = aritMatrixLeft.size();
                DataNode[] dataNodeArrayList = new DataNode[sizeMatrix];
                AritType boolType = TYPE_FACADE.getBooleanType();
                boolean res, isNumber = TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp);
                Object val1, val2;
                for (i = 0; i < sizeMatrix; i++) {
                    if (isNumber) {
                        val1 = toDouble(aritMatrixLeft.getDataNodes()[i].value);
                        val2 = toDouble(aritMatrixRight.getDataNodes()[i].value);
                    } else {
                        val1 = aritMatrixLeft.getDataNodes()[i].value;
                        val2 = aritMatrixRight.getDataNodes()[i].value;
                    }
                    res = (this.operator == Operator.EQUAL) == (val1 == val2);
                    dataNodeArrayList[i] = new DataNode(boolType, res);
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(boolType, dataNodeArrayList, aritMatrixLeft.rows, aritMatrixLeft.columns);
                return true;
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : No se puede comparar una matriz de " +
                        "tipo " + aritMatrixLeft.baseType + " con una matriz de tipo " + aritMatrixRight.baseType + ".", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en `" + this +
                    "` : No se puede comparar dos matrices con diferentes dimensiones.", this.info);
        }
        return false;
    }

    private boolean operateVectors(
            AritLanguage aritLanguage, @NotNull AritVector aritVectorLeft, @NotNull AritVector aritVectorRight
    ) {
        int sizeVectorLeft = aritVectorLeft.size();
        int sizeVectorRight = aritVectorRight.size();
        if (sizeVectorLeft == 1 || sizeVectorRight == 1 || sizeVectorLeft == sizeVectorRight) {
            AritType typeTemp = getMaxType(aritVectorLeft.baseType, aritVectorRight.baseType);
            if (TYPE_FACADE.isBooleanType(typeTemp) || TYPE_FACADE.isStringType(typeTemp) ||
                    TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp)) {
                int maxSize = Math.max(sizeVectorLeft, sizeVectorRight);
                int countLeft, countRight, i;
                ArrayList<DataNode> dataNodeArrayList = new ArrayList<>();
                AritType boolType = TYPE_FACADE.getBooleanType();
                boolean res, isNumber = TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp);
                Object val1, val2;
                for (i = 0, countLeft = 0, countRight = 0; i < maxSize; i++, countLeft++, countRight++) {
                    if (countLeft == sizeVectorLeft) countLeft = 0;
                    if (countRight == sizeVectorRight) countRight = 0;
                    if (isNumber) {
                        val1 = toDouble(aritVectorLeft.getDataNodes().get(countLeft).value);
                        val2 = toDouble(aritVectorRight.getDataNodes().get(countRight).value);
                    } else {
                        val1 = aritVectorLeft.getDataNodes().get(countLeft).value;
                        val2 = aritVectorRight.getDataNodes().get(countRight).value;
                    }
                    res = (this.operator == Operator.EQUAL) == (val1 == val2);
                    dataNodeArrayList.add(new DataNode(boolType, res));
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = new AritVector(boolType, dataNodeArrayList);
                return true;
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : No se puede comparar un vector de " +
                        "tipo " + aritVectorLeft.baseType + " con un vector de tipo " + aritVectorRight.baseType + ".", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en " + this +
                    " : No se puede comparar vectores con tamaños diferentes.", this.info);
        }
        return false;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }

    @Override
    public String toString() {
        return this.expLeft + this.operator.toString() + this.expRight;
    }
}
