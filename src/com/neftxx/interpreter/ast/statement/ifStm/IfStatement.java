package com.neftxx.interpreter.ast.statement.ifStm;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class IfStatement extends AstNode {
    public ArrayList<SubIf> subIfs;

    public IfStatement(NodeInfo info, ArrayList<SubIf> subIfs) {
        super(info);
        this.subIfs = subIfs;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object value;
        for (SubIf subIf: this.subIfs) {
            value = subIf.interpret(aritLanguage, scope);
            if (subIf.getCondValue()) return value;
        }
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [ label = \"Sentencia IF\"];\n");
        for (SubIf subIf: this.subIfs) {
            subIf.createAstGraph(astGraph);
            astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                    .append("node").append(subIf.hashCode()).append("\";\n");
        }
    }
}
