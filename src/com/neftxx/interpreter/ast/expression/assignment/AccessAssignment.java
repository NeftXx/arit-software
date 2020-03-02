package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class AccessAssignment extends Expression {
    public final Expression expression;
    public final Dimension dimension;

    public AccessAssignment(NodeInfo info, Expression expression, Dimension dimension) {
        super(info);
        this.expression = expression;
        this.dimension = dimension;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object value = this.expression.interpret(aritLanguage, scope);
        AritType type = this.expression.type;
        if (TYPE_FACADE.isUndefinedType(type)) {
            this.type = type;
            return null;
        }
        if (TYPE_FACADE.isVectorType(type) && this.dimension.numberOfAccess() == 1) {
            this.dimension.interpret(aritLanguage, scope);
            if (this.dimension.thereIsError) {
                this.type = TYPE_FACADE.getUndefinedType();
                return null;
            }
            AritVector vector = (AritVector) value;
            this.type = TYPE_FACADE.getVectorType();
            this.value = vector.getItemAssignment(this.dimension.positions[0]);
            return this.value;
        }
        this.type = TYPE_FACADE.getUndefinedType();
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("node").append(this.hashCode()).append("[label = \"Asignación Acceso\"];\n");
        this.expression.createAstGraph(astGraph);
        this.dimension.createAstGraph(astGraph);
        astGraph.append("node").append(this.hashCode()).append(" -> ").append("node")
                .append(this.expression.hashCode()).append(";\n");
        astGraph.append("node").append(this.hashCode()).append(" -> ").append("node")
                .append(this.dimension.hashCode()).append(";\n");
    }

    @Override
    public String toString() {
        return this.expression.toString() + this.dimension.toString();
    }
}
