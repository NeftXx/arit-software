package com.neftxx.interpreter.ast.scope;

import java.util.HashMap;

public class Scope {
    public final Scope previous;
    protected HashMap<String, VarSymbol> variables;

    public Scope(Scope previous) {
        this.previous = previous;
        this.variables = new HashMap<>();
    }

    public void addVariable(String id, VarSymbol varSymbol) {
        this.variables.put(id, varSymbol);
    }

    public VarSymbol getVariable(String id) {
        for (Scope scope = this; scope != null; scope = scope.previous) {
            VarSymbol found = scope.variables.get(id);
            if (found != null) return found;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Scope { " +
                "previous=" + previous +
                ", variables=" + variables +
                " }";
    }
}
