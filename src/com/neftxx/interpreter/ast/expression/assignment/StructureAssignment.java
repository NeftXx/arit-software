package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritList;
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
        for (Access access: this.accessList) {
            if (!access.isToVector()) {
                ok = false;
                aritLanguage.addSemanticError("Error en `" + this +
                        "` : No se permite este tipo de acceso en un vector `" + access + "`.", this.info);
            }
        }
        if (ok) {
            Access access = accessList.get(0);
            access.interpret(aritLanguage, scope);
            int position = access.values[0];
            if (position > 0) {
                Object value = this.expression.interpret(aritLanguage, scope);
                AritType typeTemp = this.expression.type;
                if (TYPE_FACADE.isUndefinedType(typeTemp)) {
                    aritLanguage.addSemanticError("Error en `" + this +
                            "` : al calcular el valor de la expresión.", this.info);
                } else if (TYPE_FACADE.isVectorType(typeTemp)) {
                    AritVector aritVectorTemp = (AritVector) value;
                    if (aritVectorTemp.size() == 1) {
                        if (this.expression.verifyCopy()) aritVectorTemp = aritVectorTemp.copy();
                        vector.addElement(position, aritVectorTemp.getDataNodes().get(0));
                        this.value = vector;
                        this.type = TYPE_FACADE.getVectorType();
                    } else {
                        aritLanguage.addSemanticError("Error en `" + this +
                                "` : no se puede asignar a una posición de un vector una estructura con tamaño mayor a uno.",
                                this.info);
                    }
                } else if(TYPE_FACADE.isListType(typeTemp)) {
                    AritList aritListTemp = (AritList) value;
                    if (aritListTemp.size() == 1) {
                        if (this.expression.verifyCopy()) aritListTemp = aritListTemp.copy();
                        AritList newList = new AritList(vector);
                        newList.addElement(position, aritListTemp.getDataNodes().get(0));
                        this.value = newList;
                        this.type = TYPE_FACADE.getListType();
                    } else {
                        aritLanguage.addSemanticError("Error en `" + this +
                                        "` : no se puede asignar a una posición de un vector una estructura con tamaño mayor a uno.",
                                this.info);
                    }
                } else {
                    aritLanguage.addSemanticError("Error en `" + this +
                                    "` : no se puede asignar a una posición de un vector un arreglo o matriz",
                            this.info);
                }
            } else {
                aritLanguage.addSemanticError("Error en `" + this +
                        "` : el valor de la posición de acceso debe ser igual o mayor a uno.", this.info);
            }
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
                return this.value;
            }
        }
        aritLanguage.addSemanticError("Error en `" + this +
                "` : No se encontró el objeto `" + this.id + "`.", this.info);
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {

    }
}
