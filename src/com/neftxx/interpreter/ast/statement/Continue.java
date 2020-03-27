package com.neftxx.interpreter.ast.statement;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Continue extends AstNode {
    public Continue(NodeInfo info) {
        super(info);
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        return this;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [ label = \"Continue\"];\n");
    }
}
