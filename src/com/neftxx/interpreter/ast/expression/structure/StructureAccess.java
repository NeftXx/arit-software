package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.assignment.Access;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class StructureAccess extends Expression {
    public Expression expression;
    public ArrayList<Access> accesses;
    private int currentAccess;
    private final int numberOfAccesses;

    public StructureAccess(NodeInfo info, Expression expression, ArrayList<Access> accesses) {
        super(info);
        this.expression = expression;
        this.accesses = accesses;
        this.currentAccess = 0;
        this.numberOfAccesses = this.accesses.size();
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        this.currentAccess = 0;
        this.value = null;
        this.type = TYPE_FACADE.getUndefinedType();
        Object resultExp = this.expression.interpret(aritLanguage, scope);
        getAccess(aritLanguage, scope, resultExp);
        return this.value;
    }

    private void getAccess(AritLanguage aritLanguage, Scope scope, Object result) {
        if (this.currentAccess >= this.numberOfAccesses) return;
        if (result instanceof AritVector) {
            accessToVector(aritLanguage, scope, (AritVector) result);
        } else if (result instanceof AritList) {
            accessToList(aritLanguage, scope, (AritList) result);
        } else if (result instanceof AritMatrix) {
            accessToMatrix(aritLanguage, scope, (AritMatrix) result);
        } else if (result instanceof AritArray) {
            accessToArray(aritLanguage, scope, (AritArray) result);
        } else {
            aritLanguage.addSemanticError("Error : inesperado, se encontró un objeto que no es del lenguaje.", this.info);
        }
    }

    private void accessToVector(AritLanguage aritLanguage, Scope scope, AritVector vector) {
        Access access = this.accesses.get(this.currentAccess);
        if (access.notAccessVector()) {
            aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en un vector `" +
                    access + "`.", this.info);
            this.type = TYPE_FACADE.getUndefinedType();
            this.value = null;
            return;
        } else {
            try {
                access.interpret(aritLanguage, scope);
                int position = access.values[0];
                if (position > vector.size() || position < 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a un vector con un " +
                            "índice incorrecto. El índice es negativo o mayor que el tamaño del vector.", this.info);
                    this.type = TYPE_FACADE.getUndefinedType();
                    this.value = null;
                    return;
                }
                vector = vector.getItemValue(position);
                this.type = TYPE_FACADE.getVectorType();
                this.value = vector;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : Se ha accedido a un vector con un " +
                        "índice incorrecto. El índice es negativo o mayor que el tamaño del vector.", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
        }
        this.currentAccess++;
        getAccess(aritLanguage, scope, this.value);
    }

    private void accessToList(AritLanguage aritLanguage, Scope scope, AritList list) {
        Access access = this.accesses.get(this.currentAccess);
        if (access.isTypeOneToList()) {
            try {
                access.interpret(aritLanguage, scope);
                int position = access.values[0];
                if (position > list.size() || position < 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    this.type = TYPE_FACADE.getUndefinedType();
                    this.value = null;
                    return;
                }
                list = new AritList(list.getItemValue(position));
                this.type = TYPE_FACADE.getListType();
                this.value = list;
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : Se ha accedido a una lista con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
        } else if (access.isTypeTwoToList()) {
            try {
                access.interpret(aritLanguage, scope);
                int position = access.values[0];
                if (position > list.size() || position < 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    this.type = TYPE_FACADE.getUndefinedType();
                    this.value = null;
                    return;
                }
                DataNode dataNode = list.getItemValue(position);
                if (TYPE_FACADE.isBaseType(dataNode.baseType)) {
                    this.type = TYPE_FACADE.getVectorType();
                    this.value = new AritVector(dataNode);
                } else {
                    this.type = dataNode.baseType;
                    this.value = dataNode.value;
                }
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : Se ha accedido a una lista con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
        } else {
            aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en una lista `"+
                    access + "`.", this.info);
            this.type = TYPE_FACADE.getUndefinedType();
            this.value = null;
            return;
        }
        this.currentAccess++;
        getAccess(aritLanguage, scope, this.value);
    }

    private void accessToMatrix(AritLanguage aritLanguage, Scope scope, AritMatrix matrix) {
        Access access = this.accesses.get(this.currentAccess);
        if (access.isTypeOneToMatrix()) {
            try {
                access.interpret(aritLanguage, scope);
                int row = access.values[0], column = access.values[1];
                if (row > matrix.rows || row < 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a una matriz con un " +
                            "numero de fila incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    this.type = TYPE_FACADE.getUndefinedType();
                    this.value = null;
                    return;
                }
                if (column > matrix.columns || column < 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    this.type = TYPE_FACADE.getUndefinedType();
                    this.value = null;
                    return;
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = matrix.getItemWithAccessOne(row, column);
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : Se ha accedido a una matriz con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
        } else if (access.isTypeTwoToMatrix()) {
            try {
                access.interpret(aritLanguage, scope);
                int row = access.values[0];
                if (row > matrix.rows || row < 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    this.type = TYPE_FACADE.getUndefinedType();
                    this.value = null;
                    return;
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = matrix.getItemWithAccessTwo(row);
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : Se ha accedido a una matriz con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
        } else if (access.isTypeThreeToMatrix()) {
            try {
                access.interpret(aritLanguage, scope);
                int column = access.values[0];
                if (column > matrix.columns || column < 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    this.type = TYPE_FACADE.getUndefinedType();
                    this.value = null;
                    return;
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = matrix.getItemWithAccessThree(column);
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : Se ha accedido a una matriz con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
        } else if (access.isTypeFourToMatrix()) {
            try {
                access.interpret(aritLanguage, scope);
                int position = access.values[0];
                if (position > matrix.size() || position < 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a una lista con un " +
                            "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                    this.type = TYPE_FACADE.getUndefinedType();
                    this.value = null;
                    return;
                }
                this.type = TYPE_FACADE.getVectorType();
                this.value = matrix.getItemWithAccessFour(position - 1);
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : Se ha accedido a una matriz con un " +
                        "índice incorrecto. El índice es negativo o mayor o igual que el tamaño del arreglo.", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
        }else {
            aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en una matriz `" +
                    access + "`.", this.info);
            this.type = TYPE_FACADE.getUndefinedType();
            this.value = null;
            return;
        }
        this.currentAccess++;
        getAccess(aritLanguage, scope, this.value);
    }

    private void accessToArray(AritLanguage aritLanguage, Scope scope, @NotNull AritArray array) {
        int numberOfAccess = this.numberOfAccesses - this.currentAccess;
        int numberOfDimensions = array.numberOfDimensions();
        if (numberOfAccess >= numberOfDimensions) {
            int[] indexes = getIndexes(aritLanguage, scope, numberOfDimensions, array);
            if (indexes == null) {
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
            try {
                this.type = array.principalType;
                this.value = array.getValue(0, indexes);
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : Se ha accedido a un array con un " +
                        "índice incorrecto. El índice es negativo o mayor que el tamaño del array.", this.info);
                this.type = TYPE_FACADE.getUndefinedType();
                this.value = null;
                return;
            }
        } else {
            aritLanguage.addSemanticError("Error : el numero de accesos es menor a las dimensiones del array.", this.info);
            this.type = TYPE_FACADE.getUndefinedType();
            this.value = null;
            return;
        }
        getAccess(aritLanguage, scope, this.value);
    }

    @Nullable
    private int[] getIndexes(AritLanguage aritLanguage, Scope scope, int numberOfDimensions, AritArray array) {
        int[] indexes = new int[numberOfDimensions];
        int i = numberOfDimensions - 1;
        Access access;
        for (; i >= 0; i--, this.currentAccess++) {
            access = this.accesses.get(this.currentAccess);
            if (access.notAccessVector()) {
                aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en un array " +
                        access + ".", this.info);
                return null;
            } else {
                access.interpret(aritLanguage, scope);
                indexes[i] = access.values[0] - 1;
                if (indexes[i] < 0 || indexes[i] > array.indexes[i] - 1) {
                    aritLanguage.addSemanticError("Error : Se ha accedido a un array con un " +
                            "índice incorrecto. El índice es negativo o mayor que el tamaño del array.", this.info);
                    return null;
                }
            }
        }
        return indexes;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Acceso a estructura\"];\n");
        this.expression.createAstGraph(astGraph);
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.expression.hashCode()).append("\";\n");
        astGraph.append("\"node").append(this.accesses.hashCode()).append("\" [label = \"Accesos\"]\n");
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.accesses.hashCode()).append("\";\n");
        for (Access access: this.accesses) {
            access.createAstGraph(astGraph);
            astGraph.append("\"node").append(this.accesses.hashCode()).append("\" -> \"").append("node")
                    .append(access.hashCode()).append("\";\n");
        }
    }

    @Override
    public String toString() {
        return this.expression.toString() + this.accesses;
    }
}
