package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Dimension extends AstNode {
    public final ArrayList<Expression> access;
    public final boolean isAccessToList;

    public Dimension(NodeInfo info, ArrayList<Expression> access, boolean isAccessToList) {
        super(info);
        this.access = access;
        this.isAccessToList = isAccessToList;
    }

    public int numberOfAccess() {
        return access.size();
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        for (Expression expression: this.access) {
            expression.interpret(aritLanguage, scope);
        }
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
    }

    @Override
    public String toString() {
        return this.isAccessToList ? "[" + this.access + "]" : this.access.toString();
    }
}
