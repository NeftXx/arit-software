package com.neftxx.interpreter.ast.statement;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;

import java.util.ArrayList;

public class Block extends AstNode {
    public final ArrayList<AstNode> astNodes;

    public Block(NodeInfo info, ArrayList<AstNode> astNodes) {
        super(info);
        this.astNodes = astNodes;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        if (astNodes != null) {
            Object value;
            for (AstNode astNode : astNodes) {
                value = astNode.interpret(aritLanguage, scope);
            }
        }
        return null;
    }
}
