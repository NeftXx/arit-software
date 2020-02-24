package com.neftxx.interpreter.ast.expression.function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.Block;
import com.neftxx.interpreter.ast.statement.Break;
import com.neftxx.interpreter.ast.statement.Continue;
import com.neftxx.interpreter.ast.statement.Return;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

public class Function extends Expression {
    public final String id;
    public final ArrayList<FormalParameter> parameters;
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

    public boolean verifyNamesOfParameters() {
        HashSet<String> ids = new HashSet<>();
        for (FormalParameter parameter : this.parameters)
            if (ids.add(parameter.id)) return false;
        return true;
    }

    public int getNumberOfParameters() {
        return this.parameters != null ? this.parameters.size() : 0;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object result =  block.interpret(aritLanguage, scope);
        this.type = TYPE_FACADE.getUndefinedType();
        this.value = null;
        if (result instanceof Break || result instanceof Continue) {
            aritLanguage.addSemanticError("Error en la función `" + this.id +
                    "` : No se encontró un bucle con cierre para la sentencia break o continue.", ((AstNode) result).info);
            return null;
        }
        if (result instanceof Return) {
            Return _return = (Return) result;
            this.value = _return.value;
            this.type = _return.type;
            return _return.value;
        }
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
