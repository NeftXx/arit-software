package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.*;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.scope.VarSymbol;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class StructureAssignment extends Expression {
    public String id;
    public ArrayList<Access> accessList;
    public Expression expression;

    public StructureAssignment(NodeInfo info, String id, ArrayList<Access> accessList, Expression expression) {
        super(info);
        this.id = id;
        this.accessList = accessList;
        this.expression = expression;
    }

    private void vectorAssignment(AritLanguage aritLanguage, Scope scope, AritVector vector) {
        boolean ok = true;
        for (Access access : this.accessList) {
            if (access.notAccessVector()) {
                ok = false;
                aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en un vector " +
                        access + ".", this.info);
            }
        }
        if (ok) {
            Access access = this.accessList.get(0);
            access.interpret(aritLanguage, scope);
            int position = access.values[0];
            if (position > 0) {
                Object value = this.expression.interpret(aritLanguage, scope);
                if (TYPE_FACADE.isUndefinedType(this.expression.type)) {
                    aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
                } else if (value instanceof AritVector) {
                    AritVector aritVectorTemp = (AritVector) value;
                    if (aritVectorTemp.size() == 1) {
                        try {
                            if (this.expression.verifyCopy()) aritVectorTemp = aritVectorTemp.copy();
                            vector.addElement(position, aritVectorTemp.getDataNodes().get(0));
                            this.value = vector;
                            this.type = TYPE_FACADE.getVectorType();
                        } catch (Exception ex) {
                            aritLanguage.addSemanticError("Error : al asignar en este vector.", this.info);
                        }
                    } else {
                        aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                        "vector una estructura con tamaño mayor a uno.", this.info);
                    }
                } else if (value instanceof AritList) {
                    AritList aritListTemp = (AritList) value;
                    if (aritListTemp.size() == 1) {
                        try {
                            if (this.expression.verifyCopy()) aritListTemp = aritListTemp.copy();
                            AritList newList = new AritList(vector);
                            newList.addElement(position, aritListTemp.getDataNodes().get(0));
                            this.value = newList;
                            this.type = TYPE_FACADE.getListType();
                        } catch (Exception ex) {
                            aritLanguage.addSemanticError("Error : al asignar en este vector.", this.info);
                        }
                    } else {
                        aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                        "vector una estructura con tamaño mayor a uno.", this.info);
                    }
                } else {
                    aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                    "vector un arreglo o matriz", this.info);
                }
            } else {
                aritLanguage.addSemanticError("Error : el valor de la posición de acceso debe " +
                        "ser igual o mayor a uno.", this.info);
            }
        }
    }

    private void listAssignment(AritLanguage aritLanguage, Scope scope, AritList aritList) {
        int size = this.accessList.size();
        if (size > 0) {
            int position, i;
            Access access;
            Object structure = aritList;
            for (i = 0; i < size - 1; i++) {
                access = this.accessList.get(i);
                if (structure instanceof AritList) {
                    if (access.isTypeOneToList()) {
                        access.interpret(aritLanguage, scope);
                        position = access.values[0];
                        AritList list = (AritList) structure;
                        structure = new AritList(list.getItemAssignment(position));
                    } else if (access.isTypeTwoToList()) {
                        access.interpret(aritLanguage, scope);
                        position = access.values[0];
                        AritList list = (AritList) structure;
                        structure = list.getItemAssignment(position).value;
                    } else {
                        aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en una lista " +
                                access + ".", this.info);
                        return;
                    }
                } else if (structure instanceof AritVector) {
                    if (access.notAccessVector()) {
                        aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en un vector " +
                                access + ".", this.info);
                        return;
                    } else {
                        access.interpret(aritLanguage, scope);
                        position = access.values[0];
                        AritVector vector = (AritVector) structure;
                        structure = vector.getItemAssignment(position);
                    }
                } else {
                    aritLanguage.addSemanticError("Error : No se esperaba este tipo de objeto " +
                            structure.getClass() + " en una lista en el acceso " + access + ".", this.info);
                    return;
                }
            }

            if (structure instanceof AritList) {
                AritList list = (AritList) structure;
                i = size - 1;
                access = this.accessList.get(i);
                if (access.isTypeOneToList()) {
                    access.interpret(aritLanguage, scope);
                    position = access.values[0];
                    if (position > 0) {
                        Object value = this.expression.interpret(aritLanguage, scope);
                        if (TYPE_FACADE.isUndefinedType(this.expression.type)) {
                            aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
                        } else if (value instanceof AritVector) {
                            AritVector aritVectorTemp = (AritVector) value;
                            if (aritVectorTemp.size() == 1) {
                                try {
                                    if (this.expression.verifyCopy()) aritVectorTemp = aritVectorTemp.copy();
                                    list.addElement(position, new DataNode(TYPE_FACADE.getVectorType(), aritVectorTemp));
                                    this.value = aritVectorTemp;
                                    this.type = TYPE_FACADE.getVectorType();
                                } catch (Exception ex) {
                                    aritLanguage.addSemanticError("Error : al asignar en este vector.", this.info);
                                }
                            } else {
                                aritLanguage.addSemanticError("Error : no se puede asignar a una posición de una " +
                                        "lista una estructura con tamaño mayor a uno.", this.info);
                            }
                        } else if (value instanceof AritList) {
                            AritList aritListTemp = (AritList) value;
                            if (aritListTemp.size() == 1) {
                                try {
                                    if (this.expression.verifyCopy()) aritListTemp = aritListTemp.copy();
                                    DataNode dataNodeTemp = aritListTemp.getDataNodes().get(0);
                                    list.addElement(position, dataNodeTemp);
                                    this.value = aritListTemp;
                                    this.type = TYPE_FACADE.getListType();
                                } catch (Exception ex) {
                                    aritLanguage.addSemanticError("Error : al asignar en este vector.", this.info);
                                }
                            } else {
                                aritLanguage.addSemanticError("Error : no se puede asignar a una posición de una " +
                                        "lista una estructura con tamaño mayor a uno.", this.info);
                            }
                        }  else {
                            aritLanguage.addSemanticError("Error : no se puede asignar a una posición de una " +
                                    "lista un arreglo o matriz", this.info);
                        }
                    } else {
                        aritLanguage.addSemanticError("Error : La posición para acceder a una lista debe " +
                                "ser mayor a cero." + access + ".", this.info);
                    }
                }
                else if (access.isTypeTwoToList()) {
                    access.interpret(aritLanguage, scope);
                    position = access.values[0];
                    if (position > 0) {
                        Object value = this.expression.interpret(aritLanguage, scope);
                        if (TYPE_FACADE.isUndefinedType(this.expression.type)) {
                            aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
                        } else if (value instanceof AritVector) {
                            AritVector aritVectorTemp = (AritVector) value;
                            try {
                                if (this.expression.verifyCopy()) aritVectorTemp = aritVectorTemp.copy();
                                list.addElement(position, new DataNode(TYPE_FACADE.getVectorType(), aritVectorTemp));
                                this.value = aritVectorTemp;
                                this.type = TYPE_FACADE.getVectorType();
                            } catch (Exception ex) {
                                aritLanguage.addSemanticError("Error : al asignar en este vector.", this.info);
                            }
                        } else if (value instanceof AritList) {
                            AritList aritListTemp = (AritList) value;
                            try {
                                if (this.expression.verifyCopy()) aritListTemp = aritListTemp.copy();
                                list.addElement(position, new DataNode(TYPE_FACADE.getListType(), aritListTemp));
                                this.value = aritListTemp;
                                this.type = TYPE_FACADE.getListType();
                            } catch (Exception ex) {
                                aritLanguage.addSemanticError("Error : al asignar en este vector.", this.info);
                            }
                        }  else {
                            aritLanguage.addSemanticError("Error : no se puede asignar a una posición de una " +
                                    "lista un arreglo o matriz", this.info);
                        }
                    } else {
                        aritLanguage.addSemanticError("Error : La posición para acceder a una lista debe " +
                                "ser mayor a cero." + access + ".", this.info);
                    }
                } else {
                    aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en una lista " +
                            access + ".", this.info);
                }
            } else if (structure instanceof AritVector) {
                AritVector vector = (AritVector) structure;
                i = size - 1;
                access = this.accessList.get(i);
                if (access.notAccessVector()) {
                    aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en un vector " +
                            access + ".", this.info);
                } else {
                    access.interpret(aritLanguage, scope);
                    position = access.values[0];
                    if (position > 0) {
                        Object value = this.expression.interpret(aritLanguage, scope);
                        if (value instanceof AritVector) {
                            AritVector aritVectorTemp = (AritVector) value;
                            if (aritVectorTemp.size() == 1) {
                                try {
                                    if (this.expression.verifyCopy()) aritVectorTemp = aritVectorTemp.copy();
                                    vector.addElement(position, aritVectorTemp.getDataNodes().get(0));
                                    this.value = vector;
                                    this.type = TYPE_FACADE.getVectorType();
                                } catch (Exception ex) {
                                    aritLanguage.addSemanticError("Error : al asignar en este vector.", this.info);
                                }
                            } else {
                                aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                        "vector una estructura con tamaño mayor a uno.", this.info);
                            }
                        } else {
                            aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                    "vector. Un arreglo, lista o matriz.", this.info);
                        }
                    } else {
                        aritLanguage.addSemanticError("Error : La posición para acceder a un vector debe " +
                                "ser mayor a cero." + access + ".", this.info);
                    }
                }
            } else {
                aritLanguage.addSemanticError("Error : No se esperaba este tipo de objeto " +
                        structure.getClass() + ".", this.info);
            }
        }
    }

    private void matrixAssignment(AritLanguage aritLanguage, Scope scope, AritMatrix matrix) {
        if (this.accessList.size() > 0) {
            Access access = this.accessList.get(0);
            if (access.isTypeOneToMatrix()) {
                matrixAssignmentTypeOne(aritLanguage, scope, matrix, access);
            } else if (access.isTypeTwoToMatrix()) {
                matrixAssignmentTypeTwo(aritLanguage, scope, matrix, access);
            } else if (access.isTypeThreeToMatrix()) {
                matrixAssignmentTypeThree(aritLanguage, scope, matrix, access);
            } else if (access.isTypeFourToMatrix()) {
                matrixAssignmentTypeFour(aritLanguage, scope, matrix, access);
            } else {
                aritLanguage.addSemanticError("Error : No se permite este tipo de acceso en una matriz `" +
                        access + "`.", this.info);
            }
        }
    }

    private void matrixAssignmentTypeOne(AritLanguage aritLanguage, Scope scope, AritMatrix matrix, @NotNull Access access) {
        access.interpret(aritLanguage, scope);
        int posX = access.values[0], posY = access.values[1];
        if (posX > -1 && posY > -1) {
            Object value = this.expression.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(this.expression.type)) {
                aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", access.info);
            } else if (value instanceof AritVector) {
                AritVector aritVector = (AritVector) value;
                if (aritVector.size() == 1) {
                    try {
                        this.type = TYPE_FACADE.getVectorType();
                        this.value = matrix.modifyItemWithAccessOne(posX, posY, aritVector.getDataNodes().get(0));
                    } catch (Exception ex) {
                        aritLanguage.addSemanticError("Error : se excedió el índice de la matriz.", access.info);
                        this.type = TYPE_FACADE.getUndefinedType();
                        this.value = null;
                    }
                } else {
                    aritLanguage.addSemanticError("Error : no se le puede asignar un vector de tamaño mayor " +
                                    "a uno a una matriz con este tipo de acceso", access.info);
                }
            } else {
                aritLanguage.addSemanticError("Error : no se le puede asignar un valor de tipo `" +
                        this.expression.type + "` a una matriz.", access.info);
            }
        } else {
            aritLanguage.addSemanticError("Error : las posiciones de acceso deben ser positivos.", access.info);
        }
    }

    private void matrixAssignmentTypeTwo(AritLanguage aritLanguage, Scope scope, AritMatrix matrix, @NotNull Access access) {
        access.interpret(aritLanguage, scope);
        int row = access.values[0];
        if (row > -1) {
            Object value = this.expression.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(this.expression.type)) {
                aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
            } else if (value instanceof AritVector) {
                AritVector aritVector = (AritVector) value;
                int size = aritVector.size();
                if (size == 1 || size == matrix.columns) {
                    try {
                        this.type = TYPE_FACADE.getVectorType();
                        this.value = matrix.modifyItemWithAccessTwo(aritVector.baseType, row, aritVector.getDataNodes());
                    } catch (Exception ex) {
                        aritLanguage.addSemanticError("Error : se excedió el índice de la matriz.", access.info);
                        this.type = TYPE_FACADE.getUndefinedType();
                        this.value = null;
                    }
                } else {
                    aritLanguage.addSemanticError("Error : se debe asignar un vector de tamaño 1 o del mismo " +
                                    "tamaño de las columnas de la matriz", access.info);
                }
            } else {
                aritLanguage.addSemanticError("Error : no se le puede asignar un valor de tipo `" +
                        this.expression.type + "` a una matriz.", access.info);
            }
        } else {
            aritLanguage.addSemanticError("Error : las posición de acceso a filas deben ser positivo.", access.info);
        }
    }

    private void matrixAssignmentTypeThree(AritLanguage aritLanguage, Scope scope, AritMatrix matrix, @NotNull Access access) {
        access.interpret(aritLanguage, scope);
        int column = access.values[0];
        if (column > -1) {
            Object value = this.expression.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(this.expression.type)) {
                aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
            } else if (value instanceof AritVector) {
                AritVector aritVector = (AritVector) value;
                int size = aritVector.size();
                if (size == 1 || size == matrix.rows) {
                    try {
                        this.type = TYPE_FACADE.getVectorType();
                        this.value = matrix.modifyItemWithAccessThree(aritVector.baseType, column, aritVector.getDataNodes());
                    } catch (Exception ex) {
                        aritLanguage.addSemanticError("Error : se excedió el índice de la matriz.", access.info);
                        this.type = TYPE_FACADE.getUndefinedType();
                        this.value = null;
                    }
                } else {
                    aritLanguage.addSemanticError("Error : se debe asignar un vector de tamaño 1 o del mismo " +
                                    "tamaño de las filas de la matriz", access.info);
                }
            } else {
                aritLanguage.addSemanticError("Error : no se le puede asignar un valor de tipo `"
                        + this.expression.type + "` a una matriz.", access.info);
            }
        } else {
            aritLanguage.addSemanticError("Error : las posición de acceso a columnas deben ser positivo.", access.info);
        }
    }

    private void matrixAssignmentTypeFour(AritLanguage aritLanguage, Scope scope, AritMatrix matrix, @NotNull Access access) {
        access.interpret(aritLanguage, scope);
        int position = access.values[0];
        if (position > -1) {
            Object value = this.expression.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(this.expression.type)) {
                aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
            } else if (value instanceof AritVector) {
                AritVector aritVector = (AritVector) value;
                if (aritVector.size() == 1) {
                    try {
                        position = position - 1;
                        this.type = TYPE_FACADE.getVectorType();
                        this.value = matrix.modifyItemWithAccessFour(position, aritVector.getDataNodes().get(0));
                    } catch (Exception ex) {
                        aritLanguage.addSemanticError("Error : se excedió el índice de la matriz.", access.info);
                        this.type = TYPE_FACADE.getUndefinedType();
                        this.value = null;
                    }
                } else {
                    aritLanguage.addSemanticError("Error : se debe asignar un vector de " +
                            "tamaño 1 en este tipo de acceso", access.info);
                }
            } else {
                aritLanguage.addSemanticError("Error : no se le puede asignar un valor de tipo `" +
                        this.expression.type + "` a una matriz.", access.info);
            }
        } else {
            aritLanguage.addSemanticError("Error : las posición de en este tipo de acceso deben ser " +
                    "positivo.", access.info);
        }
    }

    private void arrayAssignment(AritLanguage aritLanguage, Scope scope, @NotNull AritArray array) {
        int size = this.accessList.size();
        int numberOfDimensions = array.numberOfDimensions();
        if (size >= numberOfDimensions) {
            int[] indexes = getIndexes(aritLanguage, scope, numberOfDimensions, array);
            if (indexes == null) return;
            try {
                if (TYPE_FACADE.isVectorType(array.principalType)) {
                    Object value = this.expression.interpret(aritLanguage, scope);
                    if (value instanceof AritVector) {
                        AritVector vector = (AritVector) value;
                        if (vector.size() == 1) {
                            try {
                                array.setValueVector(0, indexes, vector.baseType, vector);
                            } catch (Exception ex) {
                                aritLanguage.addSemanticError("Error : Se ha accedido a un array con un " +
                                        "índice incorrecto. El índice es negativo o mayor que el tamaño del array.", this.info);
                            }
                        } else {
                            aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                    "arreglo una estructura con tamaño mayor a uno.", this.info);
                        }
                    } else if (value instanceof AritList) {
                        AritList list = (AritList) value;
                        if (list.size() == 1) {
                            try {
                                AritType listType = TYPE_FACADE.getListType();
                                array.setValueVector(0, indexes, listType, list);
                            } catch (Exception ex) {
                                aritLanguage.addSemanticError("Error : Se ha accedido a un array con un " +
                                        "índice incorrecto. El índice es negativo o mayor que el tamaño del array.", this.info);
                            }
                        } else {
                            aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                    "arreglo una estructura con tamaño mayor a uno.", this.info);
                        }
                    } else {
                        aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                "arreglo un matriz o arreglo.", this.info);
                    }
                } else if (TYPE_FACADE.isListType(array.principalType)) {
                    Object value = this.expression.interpret(aritLanguage, scope);
                    AritList list;
                    if (value instanceof AritVector) {
                        list = new AritList((AritVector) value);
                    } else if (value instanceof AritList) {
                        AritList tempList = (AritList) value;
                        if (this.expression.verifyCopy()) tempList = tempList.copy();
                        list = tempList;
                    } else {
                        aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                "arreglo una matriz o un arreglo.", this.info);
                        return;
                    }
                    if (list.size() == 1) {
                        try {
                            array.setValueList(0, indexes, list);
                        } catch (Exception ex) {
                            aritLanguage.addSemanticError("Error : Se ha accedido a un array con un " +
                                    "índice incorrecto. El índice es negativo o mayor que el tamaño del array.", this.info);
                        }
                    } else {
                        aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                "arreglo una estructura con tamaño mayor a uno.", this.info);
                    }
                } else {
                    aritLanguage.addSemanticError("Error : inesperado no se esperaba este tipo " + array.principalType
                            + " de arreglo.", this.info);
                }
            } catch (Exception ex) {
                aritLanguage.addSemanticError("Error : al asignar en arreglo.", this.info);
            }
        } else {
            aritLanguage.addSemanticError("Error : el numero de accesos es menor a la cantidad de dimensiones " +
                    "del arreglo.", this.info);
        }
    }

    @Nullable
    private int[] getIndexes(AritLanguage aritLanguage, Scope scope, int numberOfDimensions, AritArray array) {
        int[] indexes = new int[numberOfDimensions];
        int i = numberOfDimensions - 1;
        int j = 0;
        Access access;
        for (; i >= 0; i--, j++) {
            access = this.accessList.get(j);
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
    public Object interpret(AritLanguage aritLanguage, @NotNull Scope scope) {
        this.value = null;
        this.type = TYPE_FACADE.getUndefinedType();
        VarSymbol varSymbol = scope.getVariable(this.id);
        if (varSymbol != null) {
            if (this.id.equals("v")) {
                System.out.println();
            }
            if (varSymbol.value instanceof AritVector) {
                vectorAssignment(aritLanguage, scope, (AritVector) varSymbol.value);
                if (!TYPE_FACADE.isUndefinedType(this.type)) {
                    varSymbol.changeValues(this.type, this.value, this.info.line);
                }
            } else if (varSymbol.value instanceof AritList) {
                listAssignment(aritLanguage, scope, (AritList) varSymbol.value);
            } else if (varSymbol.value instanceof AritMatrix) {
                matrixAssignment(aritLanguage, scope, (AritMatrix) varSymbol.value);
            } else if (varSymbol.value instanceof AritArray) {
                arrayAssignment(aritLanguage, scope, (AritArray) varSymbol.value);
            }
            return this.value;
        }
        aritLanguage.addSemanticError("Error : No se encontró el objeto `" + this.id + "`.", this.info);
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Asignación(")
                .append(this.id).append(")\"];\n");
        astGraph.append("\"node").append(this.accessList.hashCode()).append("\" [label = \"Accesos\"]\n");
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.accessList.hashCode()).append("\";\n");
        for (Access access: this.accessList) {
            access.createAstGraph(astGraph);
            astGraph.append("\"node").append(this.accessList.hashCode()).append("\" -> \"").append("node")
                    .append(access.hashCode()).append("\";\n");
        }
        this.expression.createAstGraph(astGraph);
        astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                .append(this.expression.hashCode()).append("\";\n");
    }

    @Override
    public String toString() {
        StringBuilder cad = new StringBuilder(this.id);
        int i, size = this.accessList.size();
        for (i = 0; i < size; i++) {
            cad.append(this.accessList.get(i));
        }
        cad.append(" = ");
        cad.append(this.expression);
        return cad.toString();
    }
}
