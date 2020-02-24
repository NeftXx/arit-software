package com.neftxx.interpreter.ast.expression;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Literal extends Expression {
    private AritType baseType;
    private Object baseValue;

    public Literal(NodeInfo info, AritType type, Object value) {
        super(info);
        this.baseType = type;
        this.baseValue = value;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        this.value = new AritVector(this.baseType, this.baseValue);
        this.type = TYPE_FACADE.getVectorType();
        return this.value;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("node").append(this.hashCode()).append("[label = \"Literal(")
                .append(this.baseValue).append(")\"];\n");
        this.baseType.createAstGraph(astGraph);
        astGraph.append("node").append(this.hashCode()).append(" -> ").append("node")
                .append(this.baseType.hashCode()).append(";\n");
    }

    @Override
    public String toString() {
        return this.baseValue != null ? this.baseValue.toString() : "NULL";
    }
}
