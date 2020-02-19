package com.neftxx.interpreter.ast.statement.function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Block;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class Function extends AstNode {
    public final String id;
    private final ArrayList<FormalParameter> parameters;
    private final Block block;

    public Function(NodeInfo info, String id, ArrayList<FormalParameter> parameters, Block block) {
        super(info);
        this.id = id;
        this.parameters = parameters;
        this.block = block;
    }

    public int numberOfParameters() {
        return this.parameters.size();
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object result =  block.interpret(aritLanguage, scope);
        return null;
    }
}
