package com.neftxx.interpreter.ast.scope;

import com.neftxx.interpreter.ast.type.AritType;

import java.util.ArrayList;

public class VarSymbol {
    public String id;
    public AritType type;
    public Object value;
    public int declarationLine;
    public ArrayList<Integer> referenceLines;

    public VarSymbol(String id, AritType type, Object value, int declarationLine) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.declarationLine = declarationLine;
        this.referenceLines = new ArrayList<>();
    }

    public void changeValues(AritType type, Object value, int newReference) {
        this.type = type;
        this.value = value;
        this.referenceLines.add(newReference);
    }
}
