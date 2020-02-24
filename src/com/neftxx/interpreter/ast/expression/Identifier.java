package com.neftxx.interpreter.ast.expression;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.structure.AritStructure;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.scope.VarSymbol;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Identifier extends Expression {
    public final String name;

    public Identifier(NodeInfo info, String name) {
        super(info);
        this.name = name;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, @NotNull Scope scope) {
        VarSymbol varSymbol = scope.getVariable(this.name);
        if (varSymbol != null) {
            this.type = varSymbol.type;
            this.value = varSymbol.value;
            if (TYPE_FACADE.isStructureType(this.type)) {
                AritStructure structure = (AritStructure) this.value;
                return structure.copy();
            }
            return this.value;
        }
        aritLanguage.addSemanticError("Error : objeto `" + this.name + "` no encontrado.", this.info);
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("node").append(this.hashCode()).append("[ label = \"Identificador(")
                .append(this.name).append(")\"];\n");
    }

    @Override
    public String toString() {
        return this.name;
    }
}
