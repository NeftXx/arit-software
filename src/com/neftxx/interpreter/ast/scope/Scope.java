package com.neftxx.interpreter.ast.scope;

import com.neftxx.interpreter.ast.type.AritType;

import java.util.HashMap;

public class Scope {
    protected Scope previous;
    protected HashMap<String, VarSymbol> variables;

    public Scope() {
        this.previous = null;
        this.variables = new HashMap<>();
    }

    public HashMap<String, VarSymbol> getVariables() {
        return variables;
    }

    public void setPrevious(Scope previous) {
        this.previous = previous;
    }

    public void setVariables(HashMap<String, VarSymbol> variables) {
        this.variables.putAll(variables);
    }

    public void addVariable(String id, AritType type, Object value, int line) {
        VarSymbol found = getVariable(id);
        if (found != null) {
            found.changeValues(type, value, line);
        } else {
            this.variables.put(id, new VarSymbol(id, type, value, line));
        }
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
