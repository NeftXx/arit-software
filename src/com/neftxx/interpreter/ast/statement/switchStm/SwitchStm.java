package com.neftxx.interpreter.ast.statement.switchStm;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Break;
import com.neftxx.interpreter.ast.statement.Continue;
import com.neftxx.interpreter.ast.statement.Return;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        Object valueSwitch = checkIfItsNumber(getValue(aritLanguage, scope, this.expression));
        if (valueSwitch != null) {
            if (this.labels != null) {
                int i = 0;
                i = labels.stream().filter((caseStm -> caseStm.expression == null)).map(_item -> 1).reduce(i, Integer::sum);
                if (i > 1) {
                    // error
                    return null;
                }
                Scope localScope = new Scope();
                localScope.setPrevious(scope);
                localScope.setVariables(scope.getVariables());
                boolean foundLabel = false;
                Object currentValueLabel;
                CaseStm caseStmDefault = null;
                for (CaseStm caseStm: this.labels) {
                    if (caseStm.expression != null && !foundLabel) {
                        currentValueLabel = checkIfItsNumber(getValue(aritLanguage, localScope, caseStm.expression));
                        if (currentValueLabel != null) {
                            foundLabel = valueSwitch.equals(currentValueLabel);
                        } else {
                            // error
                        }
                    }

                    if (caseStm.expression == null) {
                        caseStmDefault = caseStm;
                    }

                    if (foundLabel) {
                        currentValueLabel = caseStm.interpret(aritLanguage, localScope);
                        if (currentValueLabel instanceof Break) return null;
                        if (currentValueLabel instanceof Return || currentValueLabel instanceof Continue) {
                            return currentValueLabel;
                        }
                    }
                }

                if (!foundLabel && caseStmDefault != null) {
                    Object resultDefault = caseStmDefault.interpret(aritLanguage, localScope);
                    if (resultDefault instanceof Break) return null;
                    if (resultDefault instanceof Return || resultDefault instanceof Continue) return resultDefault;
                }
            }
        } else {
            // error
        }
        return null;
    }

    private Object checkIfItsNumber(Object val) {
        if (val instanceof Integer) return ((Integer) val).doubleValue();
        return val;
    }

    @Nullable
    private Object getValue(AritLanguage aritLanguage, Scope scope, @NotNull Expression expression) {
        Object expSwitch = expression.interpret(aritLanguage, scope);
        Object value = null;
        if (TYPE_FACADE.isVectorType(expression.type)) {
            AritVector temp = (AritVector) expSwitch;
            value = temp.getDataNodes().get(0).value;
        } else if (TYPE_FACADE.isMatrixType(expression.type)) {
            AritMatrix matrix = (AritMatrix) expSwitch;
            value = matrix.getDataNodes()[0].value;
        }
        return value;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }
}
