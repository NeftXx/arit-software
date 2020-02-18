package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;

public class DataNode extends StructureNode {
    public AritType type;
    public Object value;

    public DataNode(AritType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public DataNode copy() {
        return new DataNode(type, value);
    }
}
