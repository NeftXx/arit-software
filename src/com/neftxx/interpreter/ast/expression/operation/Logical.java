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

public class Logical extends Operation {
    public enum Operator {
        AND("&"), OR("|");
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

    public Logical(NodeInfo info, Expression expLeft, Expression expRight, Operator operator) {
        super(info, expLeft, expRight);
        this.operator = operator;
    }

    @Override
    protected AritType getMaxType(AritType type1, AritType type2) {
        if (TYPE_FACADE.isBooleanType(type1) && TYPE_FACADE.isBooleanType(type2))
            return TYPE_FACADE.getBooleanType();
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
        } else if (resultLeft instanceof AritVector && resultRight instanceof AritMatrix) {
            if (operateMatrixVector(aritLanguage, (AritMatrix) resultRight, (AritVector) resultLeft, false))
                return this.value;
        }
        else {
            aritLanguage.addSemanticError("Error en " + this + " : no se puede operar las estructuras " +
                    expLeft.type + " y " + expRight.type + ".", this.info);
        }
        this.type = TYPE_FACADE.getUndefinedType();
        return null;
    }

    private boolean operateMatrixVector(
            AritLanguage aritLanguage, AritMatrix aritMatrix, @NotNull AritVector aritVector, boolean isMatrixVector
    ) {
        if (aritVector.size() == 1) {
            AritType typeTemp = getMaxType(aritMatrix.baseType, aritVector.baseType);
            if (TYPE_FACADE.isBooleanType(typeTemp)) {
                int i, sizeMatriz = aritMatrix.size();
                DataNode[] dataNodeArrayList = new DataNode[sizeMatriz];
                boolean currentValue, scalar = toBoolean(aritVector.getDataNodes().get(0).value);
                boolean res;
                for (i = 0; i < sizeMatriz; i++) {
                    currentValue = toBoolean(aritMatrix.getDataNodes()[i].value);
                    if (this.operator == Operator.AND) {
                        res = isMatrixVector ? currentValue && scalar : scalar && currentValue;
                    } else if (this.operator == Operator.OR) {
                        res = isMatrixVector ? currentValue || scalar : scalar || currentValue;
                    } else {
                        res = false;
                    }
                    dataNodeArrayList[i] = new DataNode(typeTemp, res);
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(typeTemp, dataNodeArrayList, aritMatrix.rows, aritMatrix.columns);
                return true;
            } else {
                if (isMatrixVector) {
                    aritLanguage.addSemanticError("Error en " + this + " : No se puede usar el operador " +
                            this.operator + " con una matriz de tipo " + aritMatrix.baseType +
                            " con un vector de tipo " + aritVector.baseType + ".", this.info);
                } else {
                    aritLanguage.addSemanticError("Error en " + this + " : No se puede usar el operador " +
                            this.operator + " con el vector de tipo " + aritVector.baseType +
                            " con un la matriz de tipo " + aritMatrix.baseType + ".", this.info);
                }
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
            if (TYPE_FACADE.isBooleanType(typeTemp)) {
                int i, sizeMatrix = aritMatrixLeft.size();
                DataNode[] dataNodeArrayList = new DataNode[sizeMatrix];
                boolean val1, val2;
                for (i = 0; i < sizeMatrix; i++) {
                    val1 = toBoolean(aritMatrixLeft.getDataNodes()[i].value);
                    val2 = toBoolean(aritMatrixRight.getDataNodes()[i].value);
                    if (this.operator == Operator.AND) dataNodeArrayList[i] = new DataNode(typeTemp, val1 && val2);
                    else if (this.operator == Operator.OR) dataNodeArrayList[i] = new DataNode(typeTemp, val1 || val2);
                    else dataNodeArrayList[i] = new DataNode(typeTemp, false);
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(typeTemp, dataNodeArrayList, aritMatrixLeft.rows, aritMatrixLeft.columns);
                return true;
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : No se puede usar el operador " +
                        this.operator + " con una matriz de tipo " + aritMatrixLeft.baseType +
                        " con una matriz de tipo " + aritMatrixRight.baseType + ".", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en " + this +
                    " : No se puede comparar dos matrices con diferentes dimensiones.", this.info);
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
            if (TYPE_FACADE.isBooleanType(typeTemp)) {
                int maxSize = Math.max(sizeVectorLeft, sizeVectorRight);
                int countLeft, countRight, i;
                ArrayList<DataNode> dataNodeArrayList = new ArrayList<>();
                boolean val1, val2;
                for (i = 0, countLeft = 0, countRight = 0; i < maxSize; i++, countLeft++, countRight++) {
                    if (countLeft == sizeVectorLeft) countLeft = 0;
                    if (countRight == sizeVectorRight) countRight = 0;
                    val1 = toBoolean(aritVectorLeft.getDataNodes().get(countLeft).value);
                    val2 = toBoolean(aritVectorRight.getDataNodes().get(countRight).value);
                    if (this.operator == Operator.AND) dataNodeArrayList.add(new DataNode(typeTemp, val1 && val2));
                    else if (this.operator == Operator.OR) dataNodeArrayList.add(new DataNode(typeTemp, val1 || val2));
                    else dataNodeArrayList.add(new DataNode(typeTemp, false));
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = new AritVector(typeTemp, dataNodeArrayList);
                return true;
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : No se puede usar el operador " +
                        this.operator + " con un vector de tipo " + aritVectorLeft.baseType +
                        " con un vector de tipo " + aritVectorRight.baseType + ".", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en " + this +
                    " : No se puede comparar vectores con tamaños diferentes.", this.info);
        }
        return false;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Exp Lógica(")
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
        return this.expLeft.toString() + this.operator + this.expRight;
    }
}
