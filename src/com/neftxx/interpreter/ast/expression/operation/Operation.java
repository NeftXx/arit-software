package com.neftxx.interpreter.ast.expression.operation;

import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Operation extends Expression {
    public Expression expLeft;
    public Expression expRight;

    public Operation(NodeInfo info, Expression expLeft, Expression expRight) {
        super(info);
        this.expLeft = expLeft;
        this.expRight = expRight;
    }

    protected abstract AritType getMaxType(AritType type1, AritType type2);

    protected int toInt(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Double) return ((Double) value).intValue();
        return 0;
    }

    protected double toDouble(Object value) {
        if (value instanceof Integer) return ((Integer) value).doubleValue();
        if (value instanceof Long) return ((Long) value).doubleValue();
        if (value instanceof Double) return (double) value;
        return 0.0;
    }

    protected String toString(Object value) {
        return value != null ? value.toString() : null;
    }

    protected boolean toBoolean(Object value) {
        return value instanceof Boolean && (boolean) value;
    }
}
