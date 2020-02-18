package com.neftxx.interpreter.ast;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.TypeFacade;
import com.neftxx.util.NodeInfo;

public abstract class AstNode {
    public static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();
    public final NodeInfo info;

    public AstNode(NodeInfo info) {
        this.info = info;
    }

    public abstract Object interpret(AritLanguage aritLanguage, Scope scope);
}
