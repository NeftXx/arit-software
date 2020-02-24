package com.neftxx.interpreter.ast.expression.function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class DefaultArgument extends Expression {
    public DefaultArgument(NodeInfo info) {
        super(info);
        this.type = TYPE_FACADE.getDefaultType();
        this.value = this;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("node").append(this.hashCode()).append("[label = \"default\"];\n");
    }

    @Override
    public String toString() {
        return "default";
    }
}
