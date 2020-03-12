package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.assignment.Access;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class StructureAccess extends Expression {
    public Expression expression;
    public Access access;

    public StructureAccess(NodeInfo info, Expression expression, Access access) {
        super(info);
        this.expression = expression;
        this.access = access;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        Object resultExp = this.expression.interpret(aritLanguage, scope);
        AritType typeExp = this.expression.type;
        if (TYPE_FACADE.isVectorType(typeExp)) {
            if (getVector(aritLanguage, scope, (AritVector) resultExp)) return this.value;
        } else if (TYPE_FACADE.isListType(typeExp)) {
            if (getList(aritLanguage, scope, (AritList) resultExp)) return this.value;
        } else if (TYPE_FACADE.isMatrixType(typeExp)) {
            if (getMatrix(aritLanguage, scope, (AritMatrix) resultExp)) return this.value;
        } else if (TYPE_FACADE.isArrayType(typeExp)) {
        } else {
        }
        return null;
    }

    private boolean getVector(AritLanguage aritLanguage, Scope scope, AritVector resultExp) {
        if (this.access.notAccessVector()) {
            aritLanguage.addSemanticError("Error en `" + this +
                    "` : No se permite este tipo de acceso en un vector `" + access + "`.", this.info);
        } else {
            try {
                this.access.interpret(aritLanguage, scope);
                int positionCurrent = this.access.values[0];
                if (positionCurrent > resultExp.size() || positionCurrent < 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a un vector con un " +
                            "índice incorrecto. El índice es negativo o mayor que el tamaño del vector.", this.info);
                    return false;
                }
                AritVector aritVectorCurrent = resultExp.getItemValue(positionCurrent);
                this.type = TYPE_FACADE.getVectorType();
                this.value = aritVectorCurrent;
                return true;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a un vector con un " +
                        "índice incorrecto. El índice es negativo o mayor que el tamaño del vector.", this.info);
            }
        }
        return false;
    }

    private boolean getList(AritLanguage aritLanguage, Scope scope, AritList aritList) {
        if (this.access.isTypeOneToList()) {
            try {
                this.access.interpret(aritLanguage, scope);
                int positionCurrent = this.access.values[0];
                if (positionCurrent > aritList.size() || positionCurrent < 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    return false;
                }
                aritList = new AritList(aritList.getItemValue(positionCurrent));
                this.type = TYPE_FACADE.getListType();
                this.value = aritList;
                return true;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una lista con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
            }
        } else if (this.access.isTypeTwoToList()) {
            try {
                this.access.interpret(aritLanguage, scope);
                int positionCurrent = this.access.values[0];
                if (positionCurrent > aritList.size() || positionCurrent < 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    return false;
                }
                DataNode dataNode = aritList.getItemValue(positionCurrent);
                if (TYPE_FACADE.isBaseType(dataNode.type)) {
                    this.type = TYPE_FACADE.getVectorType();
                    this.value = new AritVector(dataNode);
                } else {
                    this.type = dataNode.type;
                    this.value = dataNode.value;
                }
                return true;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una lista con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en `" + this +
                    "` : No se permite este tipo de acceso en una lista `" + access + "`.", this.info);
        }
        return false;
    }

    private boolean getMatrix(AritLanguage aritLanguage, Scope scope, AritMatrix aritMatrix) {
        if (this.access.isTypeOneToMatrix()) {
            try {
                this.access.interpret(aritLanguage, scope);
                int row = this.access.values[0], column = this.access.values[1];
                if (row > aritMatrix.rows || row < 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una matriz con un " +
                            "numero de fila incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    return false;
                }
                if (column > aritMatrix.columns || column < 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    return false;
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = aritMatrix.getItemWithAccessOne(row, column);
                return true;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una matriz con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
            }
        } else if (this.access.isTypeTwoToMatrix()) {
            try {
                this.access.interpret(aritLanguage, scope);
                int row = this.access.values[0];
                if (row > aritMatrix.rows || row < 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    return false;
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = aritMatrix.getItemWithAccessTwo(row);
                return true;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una matriz con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
            }
        } else if (this.access.isTypeThreeToMatrix()) {
            try {
                this.access.interpret(aritLanguage, scope);
                int column = this.access.values[0];
                if (column > aritMatrix.columns || column < 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    return false;
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = aritMatrix.getItemWithAccessThree(column);
                return true;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una matriz con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
            }
        } else if (this.access.isTypeFourToMatrix()) {
            try {
                this.access.interpret(aritLanguage, scope);
                int position = this.access.values[0];
                if (position > aritMatrix.size() || position < 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    return false;
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = aritMatrix.getItemWithAccessFour(position);
                return true;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error en " + this + " : Se ha accedido a una matriz con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error en `" + this +
                    "` : No se permite este tipo de acceso en una matriz `" + access + "`.", this.info);
        }
        return false;
    }
    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }

    @Override
    public String toString() {
        return this.expression.toString() + this.access;
    }
}
