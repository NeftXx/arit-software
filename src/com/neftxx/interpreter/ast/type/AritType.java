package com.neftxx.interpreter.ast.type;

import org.jetbrains.annotations.NotNull;

public abstract class AritType {
    public final int priority;
    protected final String name;

    protected AritType(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("node").append(this.hashCode()).append("[label = \"Tipo(")
                .append(this.name).append(")\"];\n");
    }

    @Override
    public String toString() {
        return name;
    }
}
