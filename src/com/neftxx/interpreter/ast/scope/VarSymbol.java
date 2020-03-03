package com.neftxx.interpreter.ast.scope;

import com.neftxx.interpreter.ast.type.AritType;

public class VarSymbol {
    public String id;
    public AritType type;
    public Object value;

    public VarSymbol(String id, AritType type, Object value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public void changeValues(AritType type, Object value) {
        this.type = type;
        this.value = value;
    }
}
