package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Dimension extends AstNode {
    public boolean thereIsError;
    public final ArrayList<Expression> access;
    public int[] positions;
    public final boolean isAccessToList;

    public Dimension(NodeInfo info, @NotNull ArrayList<Expression> access, boolean isAccessToList) {
        super(info);
        this.thereIsError = false;
        this.access = access;
        this.isAccessToList = isAccessToList;
        this.positions = new int[access.size()];
    }

    public int numberOfAccess() {
        return access.size();
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        int i = 0;
        int size = this.numberOfAccess();
        AritVector vector;
        Expression expression;
        for (; i < size; i++) {
            expression = this.access.get(i);
            expression.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isVectorType(expression.type)) {
                vector = (AritVector) expression.value;
                if (vector.size() == 0) {
                    this.thereIsError = true;
                    return "";
                }
                if (!TYPE_FACADE.isIntegerType(vector.baseType)) {
                    this.thereIsError = true;
                    return "";
                }
                positions[i] = (int) vector.getDataNodes().get(0).value;
                continue;
            }
            this.thereIsError = true;
            return "";
        }
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("node").append(this.hashCode()).append("[label = \"Acceso ")
                .append(this.isAccessToList ? "[[]]" : "[]").append("\"];\n");
        for (Expression a: this.access) {
            a.createAstGraph(astGraph);
            astGraph.append("node").append(this.hashCode()).append(" -> ").append("node")
                .append(a.hashCode()).append(";\n");
        }
    }

    @Override
    public String toString() {
        return this.isAccessToList ? "[" + this.access + "]" : this.access.toString();
    }
}
