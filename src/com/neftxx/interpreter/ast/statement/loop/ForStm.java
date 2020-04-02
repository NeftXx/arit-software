package com.neftxx.interpreter.ast.statement.loop;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.*;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Block;
import com.neftxx.interpreter.ast.statement.Break;
import com.neftxx.interpreter.ast.statement.Return;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ForStm extends AstNode {
    public String id;
    public Expression expression;
    public Block block;

    public ForStm(NodeInfo info, String id, Expression expression, Block block) {
        super(info);
        this.id = id;
        this.expression = expression;
        this.block = block;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object result = this.expression.interpret(aritLanguage, scope);
        if (result instanceof AritVector) {
            return iterateVector(aritLanguage, scope, (AritVector) result);
        } if (result instanceof AritList) {
            return iterateList(aritLanguage, scope, (AritList) result);
        } if (result instanceof AritMatrix) {
            return iterateMatrix(aritLanguage, scope, (AritMatrix) result);
        } if (result instanceof AritArray) {
            return iterateArray(aritLanguage, scope, (AritArray) result);
        } else {
            aritLanguage.addSemanticError("Error : no se puede iterar este tipo de objeto.", this.info);
        }
        return null;
    }

    @Nullable
    private Object iterateVector(@NotNull AritLanguage aritLanguage, Scope scope, @NotNull AritVector vector) {
        int i, size = vector.size();
        Object valueBlock;
        Scope localScope;
        int errors = aritLanguage.errors.size();
        for (i = 1; i <= size; i++) {
            localScope = newScope(scope, TYPE_FACADE.getVectorType(), vector.getItemValue(i));
            valueBlock = this.block.interpret(aritLanguage, localScope);
            if (valueBlock instanceof Break) return null;
            if (valueBlock instanceof Return) return valueBlock;
            if (errors < aritLanguage.errors.size()) return null;
        }
        return null;
    }

    @Nullable
    private Object iterateList(@NotNull AritLanguage aritLanguage, Scope scope, @NotNull AritList list) {
        int i, size = list.size();
        Object valueBlock;
        DataNode dataNode;
        Scope localScope;
        int errors = aritLanguage.errors.size();
        Object value;
        for (i = 1; i <= size; i++) {
            dataNode = list.getItemValue(i);
            value = dataNode.value;
            if (value instanceof AritVector) {
                localScope = newScope(scope, ((AritVector) value).baseType, value);
            } else {
                localScope = newScope(scope, dataNode.baseType, value);
            }
            valueBlock = this.block.interpret(aritLanguage, localScope);
            if (valueBlock instanceof Break) return null;
            if (valueBlock instanceof Return) return valueBlock;
            if (errors < aritLanguage.errors.size()) return null;
        }
        return null;
    }

    @Nullable
    private Object iterateMatrix(@NotNull AritLanguage aritLanguage, Scope scope, @NotNull AritMatrix matrix) {
        int i, size = matrix.size();
        Object valueBlock;
        Scope localScope;
        AritType typeVector = TYPE_FACADE.getVectorType();
        int errors = aritLanguage.errors.size();
        for (i = 0; i < size; i++) {
            localScope = newScope(scope, typeVector, matrix.getItem(i));
            valueBlock = this.block.interpret(aritLanguage, localScope);
            if (valueBlock instanceof Break) return null;
            if (valueBlock instanceof Return) return valueBlock;
            if (errors < aritLanguage.errors.size()) return null;
        }
        return null;
    }

    @Nullable
    private Object iterateArray(@NotNull AritLanguage aritLanguage, Scope scope, @NotNull AritArray array) {
        ArrayList<DataNode> dataNodes = array.getDataNodes();
        int i, size = dataNodes.size();
        Object valueBlock;
        Scope localScope;
        DataNode dataNode;
        int errors = aritLanguage.errors.size();
        for (i = 0; i < size; i++) {
            dataNode = dataNodes.get(i);
            localScope = newScope(scope, dataNode.baseType, dataNode.value);
            valueBlock = this.block.interpret(aritLanguage, localScope);
            if (valueBlock instanceof Break) return null;
            if (valueBlock instanceof Return) return valueBlock;
            if (errors < aritLanguage.errors.size()) return null;
        }
        return null;
    }

    @NotNull
    private Scope newScope(Scope parent, AritType type, Object value) {
        Scope localScope = new Scope();
        localScope.setPrevious(parent);
        localScope.setVariables(parent.getVariables());
        localScope.addVariable(this.id, type, value, this.info.line);
        return localScope;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [ label = \"Sentencia FOR\"];\n");
        astGraph.append("\"node").append(this.id.hashCode() + this.hashCode())
                .append("\" [ label = \"").append(this.id).append("\"];\n");
        this.expression.createAstGraph(astGraph);
        this.block.createAstGraph(astGraph);
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                .append("node").append(this.id.hashCode() + this.hashCode()).append("\";\n");
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                .append("node").append(this.expression.hashCode()).append("\";\n");
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"")
                .append("node").append(this.block.hashCode()).append("\";\n");
    }
}
