package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.scope.VarSymbol;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PositionAssignment extends Expression {
    public final String id;
    public final ArrayList<Dimension> dimensions;
    public final Expression expression;

    public PositionAssignment(NodeInfo info, String id, ArrayList<Dimension> dimensions, Expression expression) {
        super(info);
        this.id = id;
        this.dimensions = dimensions;
        this.expression = expression;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        VarSymbol varSymbol = scope.getVariable(this.id);
        if (varSymbol != null) {
            this.type = TYPE_FACADE.getUndefinedType();
            if (TYPE_FACADE.isVectorType(varSymbol.type)) {
                return getVector(aritLanguage, scope, varSymbol);
            }
            return null;
        }
        this.type = TYPE_FACADE.getUndefinedType();
        aritLanguage.addSemanticError("Error en " + this + " : objeto `" + this.id + "` no encontrado.", this.info);
        return null;
    }

    @Nullable
    private AritVector getVector(AritLanguage aritLanguage, Scope scope, @NotNull VarSymbol varSymbol) {
        try {
            AritVector vector = (AritVector) varSymbol.value;
            int lastPosition = 1;
            for (Dimension dimension: this.dimensions) {
                if (dimension.numberOfAccess() != 1) {
                    aritLanguage.addSemanticError("Error en " + this + " : número incorrecto de subíndices.",
                            this.info);
                    return null;
                }
                dimension.interpret(aritLanguage, scope);
                Expression expTemp = dimension.access.get(0);
                if (!TYPE_FACADE.isVectorType(expTemp.type)) {
                    aritLanguage.addSemanticError("Error en " + this + " : tipo de subíndice no válido `" +
                            expTemp.type + "`.", this.info);
                    return null;
                }
                AritVector vectorTemp = (AritVector) expTemp.value;
                if (!TYPE_FACADE.isIntegerType(vectorTemp.type)) {
                    aritLanguage.addSemanticError("Error en " + this + " : tipo de subíndice no válido `" +
                            vectorTemp.type + "`", this.info);
                    return null;
                }
                DataNode dataNode = vectorTemp.getDataNodes().get(0);
                lastPosition = (int) dataNode.value;
                vector = vector.getElement(lastPosition - 1);
            }
            Object value = this.expression.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(this.expression.type)) {
                aritLanguage.addSemanticError("Error en " + this + " : al calcular el valor de la expresión asignada.",
                        this.expression.info);
                return null;
            }
            vector.addElement(lastPosition, new DataNode(this.expression.type, value));
            this.type = TYPE_FACADE.getVectorType();
            return vector.copy();
        } catch (Exception ex) {
            this.type = TYPE_FACADE.getUndefinedType();
            aritLanguage.addSemanticError("Error en " + this + " : al acceder a una posición.", this.info);
        }
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
    }

    @Override
    public String toString() {
        StringBuilder cad = new StringBuilder(this.id);
        for (Dimension dimension: this.dimensions) {
            cad.append(dimension);
        }
        return cad + " = " + this.expression;
    }
}
