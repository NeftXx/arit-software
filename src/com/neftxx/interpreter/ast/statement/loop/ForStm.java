package com.neftxx.interpreter.ast.statement.loop;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritList;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Block;
import com.neftxx.interpreter.ast.statement.Break;
import com.neftxx.interpreter.ast.statement.Return;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (TYPE_FACADE.isVectorType(this.expression.type)) {
            return iterateVector(aritLanguage, scope, (AritVector) result);
        } if (TYPE_FACADE.isListType(this.expression.type)) {
            return iterateList(aritLanguage, scope, (AritList) result);
        } if (TYPE_FACADE.isMatrixType(this.expression.type)) {
            return iterateMatrix(aritLanguage, scope, (AritMatrix) result);
        } else {
            // error
        }
        return null;
    }

    @Nullable
    private Object iterateVector(AritLanguage aritLanguage, Scope scope, @NotNull AritVector vector) {
        int i, size = vector.size();
        Object valueBlock;
        Scope localScope;
        for (i = 1; i <= size; i++) {
            localScope = newScope(scope, TYPE_FACADE.getVectorType(), vector.getItemValue(i));
            valueBlock = this.block.interpret(aritLanguage, localScope);
            if (valueBlock instanceof Break) return null;
            if (valueBlock instanceof Return) return valueBlock;
        }
        return null;
    }

    @Nullable
    private Object iterateList(AritLanguage aritLanguage, Scope scope, @NotNull AritList list) {
        int i, size = list.size();
        Object valueBlock;
        DataNode dataNode;
        Scope localScope;
        for (i = 1; i <= size; i++) {
            dataNode = list.getItemValue(i);
            localScope = newScope(scope, dataNode.type, dataNode.value);
            valueBlock = this.block.interpret(aritLanguage, localScope);
            if (valueBlock instanceof Break) return null;
            if (valueBlock instanceof Return) return valueBlock;
        }
        return null;
    }

    @Nullable
    private Object iterateMatrix(AritLanguage aritLanguage, Scope scope, @NotNull AritMatrix matrix) {
        int i, size = matrix.size();
        Object valueBlock;
        Scope localScope;
        for (i = 0; i < size; i++) {
            localScope = newScope(scope, TYPE_FACADE.getVectorType(), matrix.getItemWithAccessFour(i));
            valueBlock = this.block.interpret(aritLanguage, localScope);
            if (valueBlock instanceof Break) return null;
            if (valueBlock instanceof Return) return valueBlock;
        }
        return null;
    }

    @NotNull
    private Scope newScope(Scope parent, AritType type, Object value) {
        Scope localScope = new Scope();
        localScope.setPrevious(parent);
        localScope.setVariables(parent.getVariables());
        localScope.addVariable(this.id, type, value);
        return localScope;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }
}
