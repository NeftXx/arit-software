package com.neftxx.interpreter.ast.statement.switchStm;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SwitchStm extends AstNode {
    public Expression expression;
    public ArrayList<CaseStm> labels;

    public SwitchStm(NodeInfo info, Expression expression, ArrayList<CaseStm> labels) {
        super(info);
        this.expression = expression;
        this.labels = labels;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object expSwitch = this.expression.interpret(aritLanguage, scope);
        if (TYPE_FACADE.isVectorType(this.expression.type)) {

        } else if (TYPE_FACADE.isMatrixType(this.expression.type)) {

        }
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }
}
