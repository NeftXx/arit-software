package com.neftxx.interpreter.ast.expression.operation;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Arithmetic extends Operation {
    public enum Operator {
        SUM("+"), SUBTRACTION("-"), MULTIPLICATION("*"), POWER("^"), DIVISION("/"), MODULE("%%");

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

    public Arithmetic(NodeInfo info, Expression expLeft, Expression expRight, Operator operator) {
        super(info, expLeft, expRight);
        this.operator = operator;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object resultExpLeft = expLeft.interpret(aritLanguage, scope);
        Object resultExpRight = expRight.interpret(aritLanguage, scope);
        if (resultExpLeft instanceof AritVector && resultExpRight instanceof AritVector) {
            if (operationBetweenVectors(aritLanguage, (AritVector) resultExpLeft, (AritVector) resultExpRight))
                return this.value;
        } else if (resultExpLeft instanceof AritMatrix && resultExpRight instanceof AritMatrix) {
            if (matrixOperation(aritLanguage, (AritMatrix) resultExpLeft, (AritMatrix) resultExpRight))
                return this.value;
        } else if (resultExpLeft instanceof AritMatrix && resultExpRight instanceof AritVector) {
            return operateMatrixVector(aritLanguage, (AritMatrix) resultExpLeft, (AritVector) resultExpRight);
        } else if (resultExpLeft instanceof AritVector && resultExpRight instanceof  AritMatrix) {
            return operateVectorMatrix(aritLanguage, (AritVector) resultExpLeft, (AritMatrix) resultExpRight);
        } else if (resultExpLeft instanceof AritVector) {
            AritVector vector = (AritVector) resultExpLeft;
            if (TYPE_FACADE.isStringType(vector.baseType)) {
                int size = vector.size();
                int i;
                ArrayList<DataNode> dataNodeArrayList = new ArrayList<>();
                AritType stringType = TYPE_FACADE.getStringType();
                for (i = 0; i < size; i++) {
                    dataNodeArrayList.add(
                            getDataNode(aritLanguage, stringType, vector.getDataNodes().get(i).value, resultExpRight)
                    );
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = new AritVector(stringType, dataNodeArrayList);
                return this.value;
            } else {
                aritLanguage.addSemanticError("Error : No se puede operar un vector de tipo `" +
                        vector.baseType + "` con una estructura de tipo `" + expRight.type + "`.", this.info);
            }
        } else if (resultExpRight instanceof AritVector) {
            AritVector vector = (AritVector) resultExpRight;
            if (TYPE_FACADE.isStringType(vector.baseType)) {
                int size = vector.size();
                int i;
                ArrayList<DataNode> dataNodeArrayList = new ArrayList<>();
                AritType stringType = TYPE_FACADE.getStringType();
                for (i = 0; i < size; i++) {
                    dataNodeArrayList.add(
                            getDataNode(aritLanguage, stringType, resultExpLeft, vector.getDataNodes().get(i).value)
                    );
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = new AritVector(stringType, dataNodeArrayList);
                return this.value;
            } else {
                aritLanguage.addSemanticError("Error : No se puede operar una estructura de tipo `" +
                        expLeft.type + "` con un vector de tipo `" + vector.baseType + "`.", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en " + this + " : no se puede operar las estructuras " +
                    expLeft.type + " y " + expRight.type + ".", this.info);
        }
        this.type = TYPE_FACADE.getUndefinedType();
        return null;
    }

    @Nullable
    private Object operateVectorMatrix(
            AritLanguage aritLanguage, @NotNull AritVector resultExpLeft, AritMatrix resultExpRight
    ) {
        if (resultExpLeft.size() == 1) {
            AritType typeTemp = getMaxType(resultExpRight.baseType, resultExpLeft.baseType);
            if (TYPE_FACADE.isStringType(typeTemp) && this.operator != Operator.SUM) {
                aritLanguage.addSemanticError("Error en `" + this + "` : no se puede operar cadenas con este tipo " +
                        " de operador " + this.operator + ".", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return null;
            }
            if (TYPE_FACADE.isStringType(typeTemp) || TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp)) {
                int sizeMatriz = resultExpRight.size();
                DataNode[] dataNodeArrayList = new DataNode[sizeMatriz];
                int i;
                Object scalar = resultExpLeft.getDataNodes().get(0).value;
                for (i = 0; i < sizeMatriz; i++) {
                    dataNodeArrayList[i] = getDataNode(aritLanguage, typeTemp, scalar, resultExpRight.getDataNodes()[i].value);
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(typeTemp, dataNodeArrayList, resultExpRight.rows, resultExpRight.columns);
                return this.value;
            } else {
                aritLanguage.addSemanticError("Error en `" + this + "` : No se puede operar un objeto de tipo `" +
                        expLeft.type + "` con un objeto de tipo `" + expRight.type + "`.", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en `" + this + "` : No se puede operar una matriz con" +
                    " un vector de un tamaño mayor a uno.", this.info);
        }
        return null;
    }

    @Nullable
    private Object operateMatrixVector(
            AritLanguage aritLanguage, AritMatrix resultExpLeft, @NotNull AritVector resultExpRight
    ) {
        if (resultExpRight.size() == 1) {
            AritType typeTemp = getMaxType(resultExpLeft.baseType, resultExpRight.baseType);
            if (TYPE_FACADE.isStringType(typeTemp) && this.operator != Operator.SUM) {
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                aritLanguage.addSemanticError("Error en `" + this + "` : no se puede operar cadenas con este tipo " +
                        " de operador " + this.operator + ".", this.info);
                return null;
            }
            if (TYPE_FACADE.isStringType(typeTemp) || TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp)) {
                int i, sizeMatriz = resultExpLeft.size();
                DataNode[] dataNodeArrayList = new DataNode[sizeMatriz];
                Object scalar = resultExpRight.getDataNodes().get(0).value;
                for (i = 0; i < sizeMatriz; i++) {
                    dataNodeArrayList[i] = getDataNode(aritLanguage, typeTemp, resultExpLeft.getDataNodes()[i].value, scalar);
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(typeTemp, dataNodeArrayList, resultExpLeft.rows, resultExpLeft.columns);
                return this.value;
            } else {
                aritLanguage.addSemanticError("Error en `" + this + "` : No se puede operar un objeto de tipo `" +
                        expLeft.type + "` con un objeto de tipo `" + expRight.type + "`.", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en `" + this + "` : No se puede operar una matriz con" +
                    " un vector de un tamaño mayor a uno.", this.info);
        }
        this.type = TYPE_FACADE.getUndefinedType();
        this.value = null;
        return null;
    }

    private boolean matrixOperation(
            AritLanguage aritLanguage, @NotNull AritMatrix resultExpLeft, @NotNull AritMatrix resultExpRight
    ) {
        if (resultExpLeft.rows == resultExpRight.rows && resultExpLeft.columns == resultExpRight.columns) {
            AritType typeTemp = getMaxType(resultExpLeft.baseType, resultExpRight.baseType);
            if (TYPE_FACADE.isStringType(typeTemp) && this.operator != Operator.SUM) {
                aritLanguage.addSemanticError("Error en `" + this + "` : no se puede operar cadenas con este tipo " +
                        " de operador " + this.operator + ".", this.info);
                return false;
            }
            if (TYPE_FACADE.isStringType(typeTemp) || TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp)) {
                int i, size = resultExpLeft.size();
                DataNode[] dataNodeArrayList = new DataNode[size];
                for (i = 0; i < size; i++) {
                    dataNodeArrayList[i] = getDataNode(aritLanguage, typeTemp, resultExpLeft.getDataNodes()[i].value,
                            resultExpRight.getDataNodes()[i].value);
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(typeTemp, dataNodeArrayList, resultExpLeft.rows, resultExpRight.columns);
                return true;
            } else {
                aritLanguage.addSemanticError("Error en `" + this + "` : No se puede operar una matriz de tipo `" +
                        expLeft.type + "` con una matriz de tipo `" + expRight.type + "`.", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en `" + this +
                            "` : No se puede operar dos matrices con diferentes dimensiones.", this.info);
        }
        return false;
    }

    private boolean operationBetweenVectors(
            AritLanguage aritLanguage, @NotNull AritVector resultExpLeft, @NotNull AritVector resultExpRight
    ) {
        int sizeVectorLeft = resultExpLeft.size();
        int sizeVectorRight = resultExpRight.size();
        if (sizeVectorLeft == 1 || sizeVectorRight == 1 || sizeVectorLeft == sizeVectorRight) {
            AritType typeTemp = getMaxType(resultExpLeft.baseType, resultExpRight.baseType);
            if (TYPE_FACADE.isStringType(typeTemp) && this.operator != Operator.SUM) {
                aritLanguage.addSemanticError("Error en `" + this + "` : no se puede operar cadenas con este tipo " +
                        " de operador " + this.operator + ".", this.info);
                return false;
            }
            int maxSize = Math.max(sizeVectorLeft, sizeVectorRight);
            int countLeft, countRight, i;
            if (TYPE_FACADE.isStringType(typeTemp) || TYPE_FACADE.isIntegerType(typeTemp) || TYPE_FACADE.isNumericType(typeTemp)) {
                ArrayList<DataNode> dataNodeArrayList = new ArrayList<>();
                for (i = 0, countLeft = 0, countRight = 0; i < maxSize; i++, countLeft++, countRight++) {
                    if (countLeft == sizeVectorLeft) countLeft = 0;
                    if (countRight == sizeVectorRight) countRight = 0;
                    dataNodeArrayList.add(
                            getDataNode(aritLanguage, typeTemp, resultExpLeft.getDataNodes().get(countLeft).value,
                                    resultExpRight.getDataNodes().get(countRight).value)
                    );
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = new AritVector(typeTemp, dataNodeArrayList);
                return true;
            } else {
                aritLanguage.addSemanticError("Error en `" + this + "` : No se puede operar un vector de tipo `" +
                        expLeft.type + "` con un vector de tipo `" + expRight.type + "`.", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en `" + this + "` : la longitud de los objeto deben ser el mismo.", this.info);
        }
        return false;
    }

    @Override
    protected AritType getMaxType(AritType type1, AritType type2) {
        if (TYPE_FACADE.isIntegerType(type1) && TYPE_FACADE.isIntegerType(type2)) {
            if (this.operator == Operator.POWER) return TYPE_FACADE.getNumericType();
            return TYPE_FACADE.getIntegerType();
        }
        if (TYPE_FACADE.isIntegerType(type1) && TYPE_FACADE.isNumericType(type2) ||
                TYPE_FACADE.isNumericType(type1) && TYPE_FACADE.isIntegerType(type2) ||
                TYPE_FACADE.isNumericType(type1) && TYPE_FACADE.isNumericType(type2)) {
            return TYPE_FACADE.getNumericType();
        }
        if (TYPE_FACADE.isStringType(type1) || TYPE_FACADE.isStringType(type2)) {
            return TYPE_FACADE.getStringType();
        }
        return TYPE_FACADE.getUndefinedType();
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    private DataNode getDataNode(AritLanguage aritLanguage, AritType type, Object value1, Object value2) {
        if (TYPE_FACADE.isIntegerType(type)) return new DataNode(type, getValue(aritLanguage, value1, value2).intValue());
        else if (TYPE_FACADE.isNumericType(type)) return new DataNode(type, getValue(aritLanguage, value1, value2));
        String val1 = value1 != null ? value1.toString() : "NULL",
                val2 = value2 != null ? value2.toString() : "NULL";
        return new DataNode(type, val1 + val2);
    }

    private Double getValue(AritLanguage aritLanguage, Object value1, Object value2) {
        try {
            switch (this.operator) {
                case SUM:
                    return toDouble(toDouble(value1) + toDouble(value2));
                case SUBTRACTION:
                    return toDouble(toDouble(value1) - toDouble(value2));
                case DIVISION:
                    return toDouble(toDouble(value1) / toDouble(value2));
                case MULTIPLICATION:
                    return toDouble(toDouble(value1) * toDouble(value2));
                case POWER:
                    return toDouble(Math.pow(toDouble(value1), toDouble(value2)));
                case MODULE:
                default:
                    return toDouble(toDouble(value1) % toDouble(value2));
            }
        } catch (Exception ex) {
            aritLanguage.addSemanticError("Error en `" + this + "` : no se pudo realizar la operación numerica.", this.info);
            return Double.NaN;
        }
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Exp Aritmética(")
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
