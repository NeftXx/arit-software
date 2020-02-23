package com.neftxx.interpreter.ast.expression.function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Block;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Function extends Expression {
    public final String id;
    private final ArrayList<FormalParameter> parameters;
    private final Block block;

    public Function(NodeInfo info, String id, ArrayList<FormalParameter> parameters, Block block) {
        super(info);
        this.id = id;
        this.parameters = parameters;
        this.block = block;
    }

    public Function (NodeInfo info, String id, Block block) {
        this(info, id, null, block);
    }

    public int numberOfParameters() {
        return this.parameters != null ? this.parameters.size() : 0;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object result =  block.interpret(aritLanguage, scope);
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
    }

    @Override
    public String toString() {
        return "Function {" +
                "id='" + id + '\'' +
                ", parameters=" + parameters +
                ", block=" + block +
                '}';
    }
}
