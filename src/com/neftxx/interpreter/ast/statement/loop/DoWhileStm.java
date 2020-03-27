package com.neftxx.interpreter.ast.statement.loop;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Block;
import com.neftxx.interpreter.ast.statement.Break;
import com.neftxx.interpreter.ast.statement.Return;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class DoWhileStm extends AstNode {
    public Expression expression;
    public Block block;

    public DoWhileStm(NodeInfo info, Expression expression, Block block) {
        super(info);
        this.expression = expression;
        this.block = block;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        boolean value;
        Object valueBlock;
        do {
            Scope scopeDoWhile = new Scope();
            scopeDoWhile.setPrevious(scope);
            scopeDoWhile.setVariables(scope.getVariables());
            valueBlock = this.block.interpret(aritLanguage, scopeDoWhile);
            if (valueBlock instanceof Break) return null;
            if (valueBlock instanceof Return) return valueBlock;
            value = getCond(aritLanguage, scope);
        } while (value);
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [ label = \"Sentencia DO WHILE\"];\n");
        this.block.createAstGraph(astGraph);
        this.expression.createAstGraph(astGraph);
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                .append("node").append(this.block.hashCode()).append("\";\n");
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                .append("node").append(this.expression.hashCode()).append("\";\n");
    }

    private boolean getCond(AritLanguage aritLanguage, Scope scope) {
        Object result = this.expression.interpret(aritLanguage, scope);
        if (TYPE_FACADE.isVectorType(this.expression.type)) {
            AritVector aritVector = (AritVector) result;
            if (TYPE_FACADE.isBooleanType(aritVector.baseType)) {
                return toBoolean(aritVector.getDataNodes().get(0).value);
            }
        } else if (TYPE_FACADE.isMatrixType(this.expression.type)) {
            AritMatrix aritMatrix = (AritMatrix) result;
            if (TYPE_FACADE.isBooleanType(aritMatrix.baseType)) {
                return toBoolean(aritMatrix.getDataNodes()[0].value);
            }
        }
        // error
        return false;
    }

    private boolean toBoolean(Object value) {
        return value instanceof Boolean && (boolean) value;
    }
}
