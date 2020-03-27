package com.neftxx.interpreter.ast.statement.ifStm;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Block;
import com.neftxx.interpreter.ast.statement.Break;
import com.neftxx.interpreter.ast.statement.Continue;
import com.neftxx.interpreter.ast.statement.Return;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class SubIf extends AstNode {
    public boolean condValue, isElse;
    public Expression expression;
    public Block block;

    public SubIf(NodeInfo info, Expression expression, Block block) {
        super(info);
        this.expression = expression;
        this.block = block;
        this.isElse = false;
    }

    public SubIf(NodeInfo info, Block block) {
        super(info);
        this.expression = null;
        this.block = block;
        this.isElse = true;
    }

    public boolean getCondValue() {
        return this.condValue || this.isElse;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        boolean value = false;
        if (this.expression != null) {
            Object result = this.expression.interpret(aritLanguage, scope);
            if (result instanceof AritVector) {
                AritVector aritVector = (AritVector) result;
                if (TYPE_FACADE.isBooleanType(aritVector.baseType)) {
                    value = toBoolean(aritVector.getDataNodes().get(0).value);
                } else {
                    aritLanguage.addSemanticError("Error : se esperaba un vector de tipo boolean.", this.info);
                    return null;
                }
            } else if (result instanceof AritMatrix) {
                AritMatrix aritMatrix = (AritMatrix) result;
                if (TYPE_FACADE.isBooleanType(aritMatrix.baseType)) {
                    value = toBoolean(aritMatrix.getDataNodes()[0].value);
                } else {
                    aritLanguage.addSemanticError("Error : se esperaba una matriz de tipo boolean.", this.info);
                    return null;
                }
            } else {
                aritLanguage.addSemanticError("Error : se esperaba una estructura de tipo boolean.", this.info);
                return null;
            }
        }
        this.condValue = value;
        if (condValue || isElse) {
            Scope localScope = new Scope();
            localScope.setPrevious(scope);
            localScope.setVariables(scope.getVariables());
            Object res = this.block.interpret(aritLanguage, localScope);
            if (res instanceof Return || res instanceof Break || res instanceof Continue) return res;
        }
        return null;
    }

    private boolean toBoolean(Object value) {
        return value instanceof Boolean && (boolean) value;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        if (this.isElse) {
            astGraph.append("\"node").append(this.hashCode()).append("\" [ label = \"ELSE\"];\n");
        } else {
            astGraph.append("\"node").append(this.hashCode()).append("\"[ label = \"IF\"];\n");
            this.expression.createAstGraph(astGraph);
            astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                    .append("node").append(this.expression.hashCode()).append("\";\n");
        }
        this.block.createAstGraph(astGraph);
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                .append("node").append(this.block.hashCode()).append("\";\n");
    }
}
