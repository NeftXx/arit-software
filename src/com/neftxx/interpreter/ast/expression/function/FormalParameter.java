package com.neftxx.interpreter.ast.expression.function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class FormalParameter extends AstNode {
    public final String id;
    public Expression expDefault;

    public FormalParameter(NodeInfo info, String id, Expression expDefault) {
        super(info);
        this.id = id;
        this.expDefault = expDefault;
    }

    public FormalParameter(NodeInfo info, String id) {
        this(info, id, null);
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        return this.expDefault != null ? this.expDefault.interpret(aritLanguage, scope) : null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Parametro(")
                .append(this.id).append(")\"];\n");
        if (this.expDefault != null) {
            this.expDefault.createAstGraph(astGraph);
            astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                    .append(this.expDefault.hashCode()).append("\";\n");
        }
    }

    @Override
    public String toString() {
        return "FormalParameter{" +
                "id='" + id + '\'' +
                ", exp=" + expDefault +
                '}';
    }
}
