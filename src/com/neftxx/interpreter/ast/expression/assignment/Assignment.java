package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritStructure;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Assignment extends Expression {
    public final String id;
    public final Expression expression;

    public Assignment(NodeInfo info, String id, Expression expression) {
        super(info);
        this.id = id;
        this.expression = expression;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        this.value = expression.interpret(aritLanguage, scope);
        this.type = expression.type;
        if (expression.verifyCopy()) this.value = ((AritStructure) this.value).copy();
        scope.addVariable(this.id, this.type, this.value);
        return this.value;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("node").append(this.hashCode()).append("[label = \"Asignación(")
                .append(this.id).append(")\"];\n");
        this.expression.createAstGraph(astGraph);
        astGraph.append("node").append(this.hashCode()).append(" -> ").append("node")
                .append(this.expression.hashCode()).append(";\n");
    }

    @Override
    public String toString() {
        return this.id + " = " + this.expression;
    }
}
