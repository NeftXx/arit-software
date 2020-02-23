package com.neftxx.interpreter.ast.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

    public AritType getUndefinedType() { return UndefinedType.UNDEFINED; }

    public AritType getVectorType() { return StructureType.VECTOR; }

    public AritType getListType() { return StructureType.LIST; }

    public AritType getArrayType() { return StructureType.ARRAY; }

    public AritType getMatrixType() { return StructureType.MATRIX; }

    public boolean isIntegerType(AritType type) {
        return BaseType.INTEGER == type;
    }

    public boolean isNumericType(AritType type) {
        return BaseType.NUMERIC == type;
    }

    public boolean isBooleanType(AritType type) {
        return BaseType.BOOLEAN == type;
    }

    public boolean isStringType(AritType type) {
        return BaseType.STRING == type;
    }

    public boolean isUndefinedType(AritType type) { return UndefinedType.UNDEFINED == type; }

    public boolean isVectorType(AritType type) { return StructureType.VECTOR == type; }

    public boolean isListType(AritType type) { return StructureType.LIST == type; }

    public boolean isArrayType(AritType type) { return StructureType.ARRAY == type; }

    public boolean isMatrixType(AritType type) { return StructureType.MATRIX == type;}

    public boolean isStructureType(AritType type) { return type instanceof StructureType; }

    public Object castValue(AritType oldType, AritType newType, Object value) {
        if (isBooleanType(oldType) && isIntegerType(newType)) return castBooleanToInteger(value);
        if (isBooleanType(oldType) && isNumericType(newType)) return castBooleanToDecimal(value);
        if (isBooleanType(oldType) && isStringType(newType)) return castBooleanToString(value);
        if (isIntegerType(oldType) && isNumericType(newType)) return castIntegerToDecimal(value);
        if (isIntegerType(oldType) && isStringType(newType)) return castIntegerToString(value);
        if (isNumericType(oldType) && isStringType(newType)) return castDecimalToString(value);
        return value;
    }

    private int castBooleanToInteger(Object value) {
        return value instanceof Boolean ? ((boolean) value ? 1 : 0 ) : 0;
    }

    private double castBooleanToDecimal(Object value) {
        return value instanceof Boolean ? ((boolean) value ? 1.0 : 0.0) : 0.0;
    }

    @NotNull
    @Contract(pure = true)
    private String castBooleanToString(Object value) {
        return value instanceof Boolean ? ((boolean) value ? "True" : "False") : "True";
    }

    private double castIntegerToDecimal(Object value) {
        return value instanceof Integer ? (double) (int) value : 0;
    }

    @NotNull
    @Contract(pure = true)
    private String castIntegerToString(Object value) {
        return value instanceof Integer ? String.valueOf((int) value) : "0";
    }

    @NotNull
    @Contract(pure = true)
    private String castDecimalToString(Object value) {
        return value instanceof Double ? String.valueOf((double) value) : "0.0";
    }

    public static TypeFacade getInstance() {
        return instance;
    }
}
