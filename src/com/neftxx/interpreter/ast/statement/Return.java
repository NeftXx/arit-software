package com.neftxx.interpreter.ast.statement;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Return extends AstNode {
    public Object value;
    public AritType type;
    public Expression expression;

    public Return(NodeInfo info, Expression expression) {
        super(info);
        this.expression = expression;
    }

    public Return(NodeInfo info) {
        this(info, null);
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        if (this.expression != null) {
            this.value = this.expression.interpret(aritLanguage, scope);
            this.type = expression.type;
        } else {
            this.value = null;
            this.type = TYPE_FACADE.getUndefinedType();
        }
        return this;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }
}
