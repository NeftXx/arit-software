package com.neftxx.interpreter.ast.expression;

import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.assignment.Assignment;
import com.neftxx.interpreter.ast.expression.assignment.StructureAssignment;
import com.neftxx.interpreter.ast.expression.structure.StructureAccess;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;

public abstract class Expression extends AstNode {
    public AritType type;
    public Object value;

    public Expression(NodeInfo info) {
        super(info);
        this.type = TYPE_FACADE.getUndefinedType();
        this.value = null;
    }

    public boolean verifyCopy() {
        return (this instanceof StructureAccess || this instanceof Identifier) && TYPE_FACADE.isStructureType(this.type);
    }
}
