package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class PositionAssignment extends Expression {
    public final Expression access;
    public final Expression expression;

    public PositionAssignment(NodeInfo info, Expression access, Expression expression) {
        super(info);
        this.access = access;
        this.expression = expression;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        this.expression.interpret(aritLanguage, scope);
        if (TYPE_FACADE.isUndefinedType(this.expression.type)) return null;
        this.access.interpret(aritLanguage, scope);
        if (TYPE_FACADE.isVectorType(this.access.type)) {
            AritVector vector = (AritVector) this.access.value;
            if (TYPE_FACADE.isVectorType(this.expression.type)) {
                AritVector valueVector = (AritVector) this.expression.value;
                if (valueVector.size() != 1) {
                    return null;
                }
                if (this.expression.verifyCopy()) valueVector = valueVector.copy();
                vector.addElement(0, valueVector.getDataNodes().get(0));
            }
        }
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("node").append(this.hashCode()).append("[label = \"Asignación\"];\n");
        this.access.createAstGraph(astGraph);
        this.expression.createAstGraph(astGraph);
        astGraph.append("node").append(this.hashCode()).append(" -> ").append("node")
                .append(this.access.hashCode()).append(";\n");
        astGraph.append("node").append(this.hashCode()).append(" -> ").append("node")
                .append(this.expression.hashCode()).append(";\n");
    }

    @Override
    public String toString() {
        return this.access + " = " + this.expression;
    }
}
