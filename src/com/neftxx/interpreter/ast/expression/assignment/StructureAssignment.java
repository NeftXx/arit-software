package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritList;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.scope.VarSymbol;
import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

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
                AritType typeTemp = this.expression.type;
                if (TYPE_FACADE.isUndefinedType(typeTemp)) {
                    aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
                } else if (TYPE_FACADE.isVectorType(typeTemp)) {
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
                } else if (TYPE_FACADE.isListType(typeTemp)) {
                    AritList aritListTemp = (AritList) value;
                    if (aritListTemp.size() == 1) {
                        if (this.expression.verifyCopy()) aritListTemp = aritListTemp.copy();
                        AritList newList = new AritList(vector);
                        newList.addElement(position, aritListTemp.getDataNodes().get(0));
                        this.value = newList;
                        this.type = TYPE_FACADE.getListType();
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
                        // error
                        return;
                    }
                } else if (structure instanceof AritVector) {
                    if (access.notAccessVector()) {
                        // error
                        return;
                    } else {
                        access.interpret(aritLanguage, scope);
                        position = access.values[0];
                        AritVector vector = (AritVector) structure;
                        structure = vector.getItemAssignment(position);
                    }
                } else {
                    // error
                    return;
                }
            }

            if (structure instanceof AritList) {
                AritList list = (AritList) structure;
                i = size - 1;
                access = this.accessList.get(i);
                if (access.isTypeOneToList() || access.isTypeTwoToList()) {
                    access.interpret(aritLanguage, scope);
                    position = access.values[0];
                    Object value = this.expression.interpret(aritLanguage, scope);
                    if (value instanceof AritVector) {
                        AritVector aritVectorTemp = (AritVector) value;
                        if (aritVectorTemp.size() == 1) {
                            try {
                                if (this.expression.verifyCopy()) aritVectorTemp = aritVectorTemp.copy();
                                list.addElement(position, aritVectorTemp.getDataNodes().get(0));
                                this.value = list;
                                this.type = TYPE_FACADE.getVectorType();
                            } catch (Exception ex) {
                                // TODO: ARREGLAR ERROR
                                aritLanguage.addSemanticError("Error : al asignar en este vector.", this.info);
                            }
                        } else {
                            // TODO: ARREGLAR ERROR
                            aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                    "vector una estructura con tamaño mayor a uno.", this.info);
                        }
                    } else if (value instanceof AritList) {
                        AritList aritListTemp = (AritList) value;
                        if (aritListTemp.size() == 1) {
                            try {
                                if (this.expression.verifyCopy()) aritListTemp = aritListTemp.copy();
                                list.addElement(position, aritListTemp.getDataNodes().get(0));
                                this.value = list;
                                this.type = TYPE_FACADE.getVectorType();
                            } catch (Exception ex) {
                                // TODO: agregar error
                            }
                        } else {
                            // TODO: ARREGLAR ERROR
                            aritLanguage.addSemanticError("Error : no se puede asignar a una posición de un " +
                                    "vector una estructura con tamaño mayor a uno.", this.info);
                        }
                    } else {
                        // error
                    }
                } else {
                    // error
                }
            } else if (structure instanceof AritVector) {
                i = size - 1;
                access = this.accessList.get(i);
                access.interpret(aritLanguage, scope);
                position = access.values[0];
            } else {
                // error
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
            AritType typeTemp = this.expression.type;
            if (TYPE_FACADE.isUndefinedType(typeTemp)) {
                aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", access.info);
            } else if (TYPE_FACADE.isVectorType(typeTemp)) {
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
                        typeTemp + "` a una matriz.", access.info);
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
            AritType typeTemp = this.expression.type;
            if (TYPE_FACADE.isUndefinedType(typeTemp)) {
                aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
            } else if (TYPE_FACADE.isVectorType(typeTemp)) {
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
                        typeTemp + "` a una matriz.", access.info);
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
            AritType typeTemp = this.expression.type;
            if (TYPE_FACADE.isUndefinedType(typeTemp)) {
                aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
            } else if (TYPE_FACADE.isVectorType(typeTemp)) {
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
                        + typeTemp + "` a una matriz.", access.info);
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
            AritType typeTemp = this.expression.type;
            if (TYPE_FACADE.isUndefinedType(typeTemp)) {
                aritLanguage.addSemanticError("Error : al calcular el valor de la expresión.", this.info);
            } else if (TYPE_FACADE.isVectorType(typeTemp)) {
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
                        typeTemp + "` a una matriz.", access.info);
            }
        } else {
            aritLanguage.addSemanticError("Error : las posición de en este tipo de acceso deben ser " +
                    "positivo.", access.info);
        }
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, @NotNull Scope scope) {
        VarSymbol varSymbol = scope.getVariable(this.id);
        if (varSymbol != null) {
            if (TYPE_FACADE.isVectorType(varSymbol.type)) {
                vectorAssignment(aritLanguage, scope, (AritVector) varSymbol.value);
                if (!TYPE_FACADE.isUndefinedType(this.type)) {
                    varSymbol.changeValues(this.type, this.value);
                }
            } else if (TYPE_FACADE.isListType(varSymbol.type)) {

            } else if (TYPE_FACADE.isMatrixType(varSymbol.type)) {
                matrixAssignment(aritLanguage, scope, (AritMatrix) varSymbol.value);
            }
            return this.value;
        }
        aritLanguage.addSemanticError("Error : No se encontró el objeto `" + this.id + "`.", this.info);
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

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
