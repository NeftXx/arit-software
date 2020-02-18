package com.neftxx.interpreter.ast.error;

import com.neftxx.util.NodeInfo;

public class NodeError {
    private TypeError typeError;
    private String description;
    private NodeInfo nodeInfo;

    public NodeError(TypeError typeError, String description, NodeInfo nodeInfo) {
        this.typeError = typeError;
        this.description = description;
        this.nodeInfo = nodeInfo;
    }

    public TypeError getTypeError() {
        return typeError;
    }

    public String getDescription() {
        return description;
    }

    public int getLine() {
        return nodeInfo.line;
    }

    public int getColumn() {
        return nodeInfo.column;
    }

    public String getFilename() {
        return nodeInfo.filename;
    }
}
