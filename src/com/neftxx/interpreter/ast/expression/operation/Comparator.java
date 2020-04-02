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
        EQUAL("=="), UNEQUAL("!="),
        GREATER_THAN(">"), LESS_THAN("<"), GREATER_THAN_OR_EQUAL_TO(">="),
        LESS_THAN_OR_EQUAL_TO("<=");
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
        if (resultLeft instanceof AritVector && resultRight instanceof AritVector) {
            if (operateVectors(aritLanguage, (AritVector) resultLeft, (AritVector) resultRight)) return this.value;
        } else if (resultLeft instanceof AritMatrix && resultRight instanceof AritMatrix) {
            if (operateMatrix(aritLanguage, (AritMatrix) resultLeft, (AritMatrix) resultRight)) return this.value;
        } else if (resultLeft instanceof AritMatrix && resultRight instanceof AritVector) {
            if (operateMatrixVector(aritLanguage, (AritMatrix) resultLeft, (AritVector) resultRight, true))
                return this.value;
        } else if (resultRight instanceof AritMatrix && resultLeft instanceof AritVector) {
            if (operateMatrixVector(aritLanguage, (AritMatrix) resultRight, (AritVector) resultLeft, false))
                return this.value;
        } else {
            if (this.operator == Operator.EQUAL) {
                this.type = TYPE_FACADE.getVectorType();
                return new AritVector(TYPE_FACADE.getBooleanType(), resultLeft == resultRight);
            } else if (this.operator == Operator.UNEQUAL) {
                this.type = TYPE_FACADE.getVectorType();
                return new AritVector(TYPE_FACADE.getBooleanType(), resultLeft != resultRight);
            }
            aritLanguage.addSemanticError("Warning : no se puede operar las estructuras " +
                    expLeft.type + " y " + expRight.type + " con el operador " + this.operator + ".", this.info);
        }
        this.type = TYPE_FACADE.getVectorType();
        return new AritVector(TYPE_FACADE.getBooleanType(), false);
    }

    private boolean operateMatrixVector(
            AritLanguage aritLanguage, AritMatrix aritMatrix, @NotNull AritVector aritVector, boolean isMatrixVector
    ) {
        if (aritVector.size() == 1) {
            AritType typeTemp = getMaxType(aritMatrix.baseType, aritVector.baseType);
            boolean isNumber = TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp),
                    isBoolean = TYPE_FACADE.isBooleanType(typeTemp),
                    isString = TYPE_FACADE.isStringType(typeTemp);
            int i, sizeMatriz = aritMatrix.size();
            DataNode[] dataNodeArrayList = new DataNode[sizeMatriz];
            AritType boolType = TYPE_FACADE.getBooleanType();
            if (isNumber) {
                double currentValue, scalar = toDouble(aritVector.getDataNodes().get(0).value);
                for (i = 0; i < sizeMatriz; i++) {
                    currentValue = toDouble(aritMatrix.getDataNodes()[i].value);
                    if (isMatrixVector) dataNodeArrayList[i] = new DataNode(boolType, compareNumbers(currentValue, scalar));
                    else dataNodeArrayList[i] = new DataNode(boolType, compareNumbers(scalar, currentValue));
                }
            } else if (isBoolean) {
                if (isValid()) {
                    boolean currentValue, scalar = toBoolean(aritVector.getDataNodes().get(0).value);
                    for (i = 0; i < sizeMatriz; i++) {
                        currentValue = toBoolean(aritMatrix.getDataNodes()[i].value);
                        dataNodeArrayList[i] = new DataNode(boolType, compareBooleans(currentValue, scalar));
                    }
                } else {
                    return getMessageError(aritLanguage, aritMatrix, aritVector, isMatrixVector);
                }
            } else if (isString) {
                String currentValue, scalar = aritVector.getDataNodes().get(0).value.toString();
                for (i = 0; i < sizeMatriz; i++) {
                    currentValue = aritMatrix.getDataNodes()[i].value.toString();
                    if (isMatrixVector) dataNodeArrayList[i] = new DataNode(boolType, compareStrings(currentValue, scalar));
                    else dataNodeArrayList[i] = new DataNode(boolType, compareStrings(scalar, currentValue));
                }
            } else {
                return getMessageError(aritLanguage, aritMatrix, aritVector, isMatrixVector);
            }
            this.type = TYPE_FACADE.getMatrixType();
            this.value = new AritMatrix(boolType, dataNodeArrayList, aritMatrix.rows, aritMatrix.columns);
            return true;
        } else {
            aritLanguage.addSemanticError("Error en " + this + " : No se puede operar una matriz con" +
                    " un vector de un tamaño mayor a uno.", this.info);
        }
        return false;
    }

    private boolean getMessageError(
            AritLanguage aritLanguage, AritMatrix aritMatrix, @NotNull AritVector aritVector, boolean isMatrixVector
    ) {
        if (isMatrixVector) {
            aritLanguage.addSemanticError("Error : No se puede usar el operador " +
                    this.operator + " con una matriz de tipo " + aritMatrix.baseType +
                    " con un vector de tipo " + aritVector.baseType + ".", this.info);
        } else {
            aritLanguage.addSemanticError("Error : No se puede usar el operador " +
                    this.operator + " con el vector de tipo " + aritVector.baseType +
                    " con un la matriz de tipo " + aritMatrix.baseType + ".", this.info);
        }
        return false;
    }

    private boolean operateMatrix(
            AritLanguage aritLanguage, @NotNull AritMatrix aritMatrixLeft, @NotNull AritMatrix aritMatrixRight
    ) {
        if (aritMatrixLeft.rows == aritMatrixRight.rows && aritMatrixLeft.columns == aritMatrixRight.columns) {
            AritType typeTemp = getMaxType(aritMatrixLeft.baseType, aritMatrixRight.baseType);
            boolean isNumber = TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp),
                    isBoolean = TYPE_FACADE.isBooleanType(typeTemp),
                    isString = TYPE_FACADE.isStringType(typeTemp);
            int i, sizeMatrix = aritMatrixLeft.size();
            DataNode[] dataNodeArrayList = new DataNode[sizeMatrix];
            AritType boolType = TYPE_FACADE.getBooleanType();
            if (isNumber) {
                double val1, val2;
                for (i = 0; i < sizeMatrix; i++) {
                    val1 = toDouble(aritMatrixLeft.getDataNodes()[i].value);
                    val2 = toDouble(aritMatrixRight.getDataNodes()[i].value);
                    dataNodeArrayList[i] = new DataNode(boolType, compareNumbers(val1, val2));
                }
            } else if (isBoolean) {
                if (isValid()) {
                    boolean val1, val2;
                    for (i = 0; i < sizeMatrix; i++) {
                        val1 = toBoolean(aritMatrixLeft.getDataNodes()[i].value);
                        val2 = toBoolean(aritMatrixRight.getDataNodes()[i].value);
                        dataNodeArrayList[i] = new DataNode(boolType, compareBooleans(val1, val2));
                    }
                } else {
                    aritLanguage.addSemanticError("Warning : el usar el operador " +
                            this.operator + " con una matriz de tipo " + aritMatrixLeft.baseType +
                            " con una matriz de tipo " + aritMatrixRight.baseType + " puede dar resultados no esperados.", this.info);
                    return false;
                }
            } else if (isString) {
                String val1, val2;
                for (i = 0; i < sizeMatrix; i++) {
                    val1 = aritMatrixLeft.getDataNodes()[i].value.toString();
                    val2 = aritMatrixRight.getDataNodes()[i].value.toString();
                    dataNodeArrayList[i] = new DataNode(boolType, compareStrings(val1, val2));
                }
            } else {
                aritLanguage.addSemanticError("Warning : No se puede usar el operador " +
                        this.operator + " con una matriz de tipo " + aritMatrixLeft.baseType +
                        " con una matriz de tipo " + aritMatrixRight.baseType + " puede dar resultados no esperados.", this.info);
                return false;
            }
            this.type = TYPE_FACADE.getMatrixType();
            this.value = new AritMatrix(boolType, dataNodeArrayList, aritMatrixLeft.rows, aritMatrixLeft.columns);
            return true;
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
            boolean isNumber = TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp),
                    isBoolean = TYPE_FACADE.isBooleanType(typeTemp),
                    isString = TYPE_FACADE.isStringType(typeTemp);
            int maxSize = Math.max(sizeVectorLeft, sizeVectorRight);
            int countLeft, countRight, i;
            ArrayList<DataNode> dataNodeArrayList = new ArrayList<>();
            AritType boolType = TYPE_FACADE.getBooleanType();
            if (isNumber) {
                double val1, val2;
                for (i = 0, countLeft = 0, countRight = 0; i < maxSize; i++, countLeft++, countRight++) {
                    if (countLeft == sizeVectorLeft) countLeft = 0;
                    if (countRight == sizeVectorRight) countRight = 0;
                    val1 = toDouble(aritVectorLeft.getDataNodes().get(countLeft).value);
                    val2 = toDouble(aritVectorRight.getDataNodes().get(countRight).value);
                    dataNodeArrayList.add(new DataNode(boolType, compareNumbers(val1, val2)));
                }
            } else if (isBoolean) {
                if (isValid()) {
                    boolean val1, val2;
                    for (i = 0, countLeft = 0, countRight = 0; i < maxSize; i++, countLeft++, countRight++) {
                        if (countLeft == sizeVectorLeft) countLeft = 0;
                        if (countRight == sizeVectorRight) countRight = 0;
                        val1 = toBoolean(aritVectorLeft.getDataNodes().get(countLeft).value);
                        val2 = toBoolean(aritVectorRight.getDataNodes().get(countRight).value);
                        dataNodeArrayList.add(new DataNode(boolType, compareBooleans(val1, val2)));
                    }
                } else {
                    aritLanguage.addSemanticError("Error en " + this + " : No se puede usar el operador " +
                            this.operator + " con un vector de tipo " + aritVectorLeft.baseType +
                            " con un vector de tipo " + aritVectorRight.baseType + ".", this.info);
                    return false;
                }
            } else if (isString) {
                String str1, str2;
                for (i = 0, countLeft = 0, countRight = 0; i < maxSize; i++, countLeft++, countRight++) {
                    if (countLeft == sizeVectorLeft) countLeft = 0;
                    if (countRight == sizeVectorRight) countRight = 0;
                    str1 = toString(aritVectorLeft.getDataNodes().get(countLeft).value);
                    str2 = toString(aritVectorRight.getDataNodes().get(countRight).value);
                    dataNodeArrayList.add(new DataNode(boolType, compareStrings(str1, str2)));
                }
            } else {
                aritLanguage.addSemanticError("Warning : el usar el operador " +
                        this.operator + " con un vector de tipo " + aritVectorLeft.baseType +
                        " con un vector de tipo " + aritVectorRight.baseType + " puede dar resultados no esperados.", this.info);
                return false;
            }
            this.type = TYPE_FACADE.getVectorType();
            this.value = new AritVector(boolType, dataNodeArrayList);
            return true;
        } else {
            aritLanguage.addSemanticError("Error en " + this +
                    " : No se puede comparar vectores con tamaños diferentes.", this.info);
        }
        return false;
    }

    private boolean isValid() {
        switch (this.operator) {
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL_TO:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL_TO:
                return false;
        }
        return true;
    }

    private Boolean compareStrings(String val1, String val2) {
        switch (this.operator) {
            case EQUAL:
                if (val1 == null && val2 == null) return true;
                return val1 != null && val1.equals(val2);
            case UNEQUAL:
                if (val1 == null && val2 == null) return false;
                return !(val1 != null && val1.equals(val2));
            case LESS_THAN:
                if (val1 == null || val2 == null) return false;
                return val1.compareTo(val2) < 0;
            case GREATER_THAN:
                if (val1 == null || val2 == null) return false;
                return val1.compareTo(val2) > 0;
            case LESS_THAN_OR_EQUAL_TO:
                if (val1 == null || val2 == null) return true;
                return val1.compareTo(val2) <= 0;
            case GREATER_THAN_OR_EQUAL_TO:
                if (val1 == null || val2 == null) return true;
                return val1.compareTo(val2) >= 0;
            default:
                return false;
        }
    }

    private Boolean compareBooleans(Boolean val1, Boolean val2) {
        switch (this.operator) {
            case EQUAL:
                return val1.equals(val2);
            case UNEQUAL:
                return !val1.equals(val2);
            default:
                return false;
        }
    }

    private Boolean compareNumbers(Double val1, Double val2) {
        switch (this.operator) {
            case EQUAL:
                return val1.equals(val2);
            case UNEQUAL:
                return !val1.equals(val2);
            case LESS_THAN:
                return val1 < val2;
            case GREATER_THAN:
                return val1 > val2;
            case LESS_THAN_OR_EQUAL_TO:
                return val1 <= val2;
            case GREATER_THAN_OR_EQUAL_TO:
                return val1 >= val2;
            default:
                return false;
        }
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Exp Comparativa(")
                .append(this.operator).append(")\"];\n");
        this.expLeft.createAstGraph(astGraph);
        this.expRight.createAstGraph(astGraph);
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.expLeft.hashCode()).append("\";\n");
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.expRight.hashCode()).append("\";\n");
    }

    @Override
    public String toString() {
        return this.expLeft + this.operator.toString() + this.expRight;
    }
}
