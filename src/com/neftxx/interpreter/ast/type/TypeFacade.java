package com.neftxx.interpreter.ast.type;

public class TypeFacade {
    private static final TypeFacade instance = new TypeFacade();

    private TypeFacade() {}

    public AritType getIntegerType() {
        return BaseType.INTEGER;
    }

    public AritType getNumericType() {
        return BaseType.NUMERIC;
    }

    public AritType getBooleanType() {
        return BaseType.BOOLEAN;
    }

    public AritType getStringType() {
        return BaseType.STRING;
    }

    public AritType getNullType() { return EmptyType.NULL; }

    public AritType getUndefinedType() { return EmptyType.UNDEFINED; }

    public boolean isBaseType(AritType type) { return type instanceof BaseType; }

    public boolean isIntegerType(AritType type) {
        return BaseType.INTEGER == type;
    }

    public boolean isDecimalType(AritType type) {
        return BaseType.NUMERIC == type;
    }

    public boolean isBooleanType(AritType type) {
        return BaseType.BOOLEAN == type;
    }

    public boolean isStringType(AritType type) {
        return BaseType.STRING == type;
    }

    public boolean isNullType(AritType type) { return EmptyType.NULL == type; }

    public boolean isUndefinedType(AritType type) { return EmptyType.UNDEFINED == type; }

    public static TypeFacade getInstance() {
        return instance;
    }
}
