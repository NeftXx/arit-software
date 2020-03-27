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

public class UnarySubtraction extends Operation {

    public UnarySubtraction(NodeInfo info, Expression expression) {
        super(info, expression, null);
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object result = this.expLeft.interpret(aritLanguage, scope);
        if (result instanceof AritVector) {
            AritVector aritVector = (AritVector) result;
            if (TYPE_FACADE.isIntegerType(aritVector.baseType)) {
                int i, size = aritVector.size();
                ArrayList<DataNode> dataNodeArrayList = new ArrayList<>();
                for (i = 0; i < size; i++) {
                    dataNodeArrayList.add(new DataNode(aritVector.baseType, -toInt(aritVector.getDataNodes().get(i).value)));
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = new AritVector(aritVector.baseType, dataNodeArrayList);
                return this.value;
            } else if (TYPE_FACADE.isNumericType(aritVector.baseType)) {
                int i, size = aritVector.size();
                ArrayList<DataNode> dataNodeArrayList = new ArrayList<>();
                for (i = 0; i < size; i++) {
                    dataNodeArrayList.add(new DataNode(aritVector.baseType, -toDouble(aritVector.getDataNodes().get(i).value)));
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = new AritVector(aritVector.baseType, dataNodeArrayList);
                return this.value;
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : no se puede operar con el signo '-' a " +
                        " un vector de tipo " + aritVector.baseType + ".", this.info);
            }
        } else if (result instanceof AritMatrix) {
            AritMatrix aritMatrix = (AritMatrix) result;
            if (TYPE_FACADE.isIntegerType(aritMatrix.baseType)) {
                int i, size = aritMatrix.size();
                DataNode[] dataNodes = new DataNode[size];
                for (i = 0; i < size; i++) {
                    dataNodes[i] = new DataNode(aritMatrix.baseType, -toInt(aritMatrix.getDataNodes()[i].value));
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(aritMatrix.baseType, dataNodes, aritMatrix.rows, aritMatrix.columns);
                return this.value;
            } else if (TYPE_FACADE.isNumericType(aritMatrix.baseType)) {
                int i, size = aritMatrix.size();
                DataNode[] dataNodes = new DataNode[size];
                for (i = 0; i < size; i++) {
                    dataNodes[i] = new DataNode(aritMatrix.baseType, -toDouble(aritMatrix.getDataNodes()[i].value));
                }
                this.type = TYPE_FACADE.getMatrixType();
                this.value = new AritMatrix(aritMatrix.baseType, dataNodes, aritMatrix.rows, aritMatrix.columns);
                return this.value;
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : no se puede operar con el signo '-' a " +
                        " una matriz de tipo " + aritMatrix.baseType + ".", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en " + this + " : no se puede operar con el signo '-' a " +
                    " una estructura de tipo " + this.expLeft.type + ".", this.info);
        }
        this.type = TYPE_FACADE.getUndefinedType();
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Exp Aritmética(-)\"];\n");
        this.expLeft.createAstGraph(astGraph);
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.expLeft.hashCode()).append("\";\n");
    }

    @Override
    protected AritType getMaxType(AritType type1, AritType type2) {
        return this.type;
    }

    @Override
    public String toString() {
        return "-" + this.expLeft;
    }
}
