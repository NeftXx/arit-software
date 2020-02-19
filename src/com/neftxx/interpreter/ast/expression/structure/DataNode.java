package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.interpreter.ast.type.TypeFacade;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataNode extends StructureNode {
    public AritType type;
    public Object value;

    public DataNode(AritType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public void changeValues(AritType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public DataNode copy() {
        return new DataNode(type, value);
    }

    @Nullable
    public static DataNode getDataNodeDefault(AritType type) {
        if (TYPE_FACADE.isIntegerType(type)) return getDataNodeInteger();
        if (TYPE_FACADE.isNumericType(type)) return getDataNodeNumeric();
        if (TYPE_FACADE.isBooleanType(type)) return getDataNodeBoolean();
        if (TYPE_FACADE.isStringType(type)) return getDataNodeString();
        return null;
    }

    @NotNull
    @Contract(" -> new")
    private static DataNode getDataNodeInteger() {
        return new DataNode(TYPE_FACADE.getIntegerType(), 0);
    }

    @NotNull
    @Contract(" -> new")
    private static DataNode getDataNodeNumeric() {
        return new DataNode(TYPE_FACADE.getNumericType(), 0.00);
    }

    @NotNull
    @Contract(" -> new")
    private static DataNode getDataNodeBoolean() {
        return new DataNode(TYPE_FACADE.getBooleanType(), false);
    }

    @NotNull
    @Contract(" -> new")
    private static DataNode getDataNodeString() {
        return new DataNode(TYPE_FACADE.getStringType(), null);
    }

    private static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();

    @Override
    public String toString() {
        return this.value != null ? this.value.toString() : "NULL";
    }
}
