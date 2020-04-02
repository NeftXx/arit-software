package com.neftxx.interpreter.ast.expression.operation;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritStructure;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Ternary extends Operation {
    public Expression condition;

    public Ternary(NodeInfo info, Expression condition, Expression expLeft, Expression expRight) {
        super(info, expLeft, expRight);
        this.condition = condition;
    }

    @Override
    protected AritType getMaxType(AritType type1, AritType type2) {
        return this.type;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        this.type = TYPE_FACADE.getUndefinedType();
        Object resultCond = this.condition.interpret(aritLanguage, scope);
        boolean result = false;
        if (resultCond instanceof AritVector) {
            AritVector aritVector = (AritVector) resultCond;
            if (TYPE_FACADE.isBooleanType(aritVector.baseType)) {
                if (aritVector.size() > 0) {
                    result = toBoolean(aritVector.getDataNodes().get(0).value);
                }
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : no se puede realizar la operación ternaria de " +
                        " un vector de tipo " + aritVector.baseType + ".", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                return null;
            }
        } else if (resultCond instanceof AritMatrix) {
            AritMatrix aritMatrix = (AritMatrix) resultCond;
            if (TYPE_FACADE.isBooleanType(aritMatrix.baseType)) {
                if (aritMatrix.size() > 0) {
                    result = toBoolean(aritMatrix.getDataNodes()[0].value);
                }
            } else {
                aritLanguage.addSemanticError("Error en " + this + " : no se puede realizar la operación ternaria de " +
                        " una matriz de tipo " + aritMatrix.baseType + ".", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                return null;
            }
        } else {
            aritLanguage.addSemanticError("Error en " + this + " : no se puede realizar la operación ternaria de una " +
                    " estructura de tipo " + this.expLeft.type + ".", this.info);
            this.type = TYPE_FACADE.getUndefinedType();
            return null;
        }
        if (result) {
            this.value = this.expLeft.interpret(aritLanguage, scope);
            this.type = this.expLeft.type;
            if (this.expLeft.verifyCopy()) this.value = ((AritStructure) this.value).copy();
            return this.value;
        }
        this.value = this.expRight.interpret(aritLanguage, scope);
        this.type = this.expRight.type;
        if (this.expRight.verifyCopy()) this.value = ((AritStructure) this.value).copy();
        return this.value;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Exp Ternario\"];\n");
        this.condition.createAstGraph(astGraph);
        this.expLeft.createAstGraph(astGraph);
        this.expRight.createAstGraph(astGraph);
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.condition.hashCode()).append("\";\n");
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.expLeft.hashCode()).append("\";\n");
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.expRight.hashCode()).append("\";\n");
    }

    @Override
    public String toString() {
        return this.condition + " ? " + this.expLeft + " : " + this.expRight;
    }
}
