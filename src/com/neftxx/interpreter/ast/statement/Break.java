package com.neftxx.interpreter.ast.statement;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Break extends AstNode {
    public Break(NodeInfo info) {
        super(info);
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        return this;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }
}
