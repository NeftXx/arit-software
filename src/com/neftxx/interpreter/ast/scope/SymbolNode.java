package com.neftxx.interpreter.ast.scope;

public class SymbolNode {
    public String id;
    public String type;
    public int size;
    public String scope;
    public int declarationLine;
    public String linesReference;

    public SymbolNode(String id, String type, int size, String scope, int declarationLine, String linesReference) {
        this.id = id;
        this.type = type;
        this.size = size;
        this.scope = scope;
        this.declarationLine = declarationLine;
        this.linesReference = linesReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getDeclarationLine() {
        return declarationLine;
    }

    public void setDeclarationLine(int declarationLine) {
        this.declarationLine = declarationLine;
    }

    public String getLinesReference() {
        return linesReference;
    }

    public void setLinesReference(String linesReference) {
        this.linesReference = linesReference;
    }
}
