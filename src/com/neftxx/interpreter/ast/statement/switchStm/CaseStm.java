package com.neftxx.interpreter.ast.statement.switchStm;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Break;
import com.neftxx.interpreter.ast.statement.Continue;
import com.neftxx.interpreter.ast.statement.Return;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CaseStm extends AstNode {
    public Expression expression;
    public ArrayList<AstNode> astNodes;

    public CaseStm(NodeInfo info, Expression expression, ArrayList<AstNode> astNodes) {
        super(info);
        this.expression = expression;
        this.astNodes = astNodes;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        if (this.astNodes != null) {
            for (AstNode astNode: this.astNodes) {
                Object value = astNode.interpret(aritLanguage, scope);
                if (value instanceof Break || value instanceof Continue || value instanceof Return)
                    return value;
            }
        }
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        if (this.expression == null) {
            astGraph.append("\"node").append(this.hashCode()).append("\" [ label = \"DEFAULT\"];\n");
        } else {
            astGraph.append("\"node").append(this.hashCode()).append("\" [ label = \"CASE\"];\n");
            this.expression.createAstGraph(astGraph);
            astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                    .append("node").append(this.expression.hashCode()).append("\";\n");
        }
        if (astNodes != null) {
            for (AstNode astNode: astNodes) {
                astNode.createAstGraph(astGraph);
                astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                        .append("node").append(astNode.hashCode()).append("\";\n");
            }
        }
    }
}
