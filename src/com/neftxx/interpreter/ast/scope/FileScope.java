package com.neftxx.interpreter.ast.scope;

import java.util.HashMap;

public class FileScope extends Scope {
    public HashMap<String, MethodSymbol> methods;

    public FileScope() {
        super(null);
        this.methods = new HashMap<>();
    }

    public boolean addMethod(String id, MethodSymbol methodSymbol) {
        if (this.methods.containsKey(id)) return false;
        this.methods.put(id, methodSymbol);
        return true;
    }

    public MethodSymbol getMethod(String id) {
        return methods.get(id);
    }

    @Override
    public String toString() {
        return "FileScope {" +
                "methods=" + methods +
                ", previous=" + previous +
                ", variables=" + variables +
                " }";
    }
}
