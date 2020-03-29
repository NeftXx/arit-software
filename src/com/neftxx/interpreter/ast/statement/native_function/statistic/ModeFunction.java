package com.neftxx.interpreter.ast.statement.native_function.statistic;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ModeFunction extends NativeFunction {
    public ModeFunction() {
        super("mode");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        int argumentsSize = arguments.size();
        this.type = TYPE_FACADE.getUndefinedType();
        if (argumentsSize == 1) {
            Expression expression = arguments.get(0);
            Object resultExp = expression.interpret(aritLanguage, scope);
            if (resultExp instanceof AritVector) {
                AritVector vector = (AritVector) resultExp;
                if (isNumeric(vector)) {
                    int i;
                    int size = vector.size();
                    double[] values = new double[size];
                    for (i = 0; i < size; i++) {
                        values[i] = toNumeric(vector.getDataNodes().get(i).value);
                    }
                    this.type = TYPE_FACADE.getVectorType();
                    double mode = StatisticalOperations.calculateMode(values);
                    return new AritVector(new DataNode(TYPE_FACADE.getNumericType(), mode));
                } else {
                    aritLanguage.addSemanticError("Error : se esperaba un vector de tipo numerico en la función `mode()`.", info);
                }
            } else {
                aritLanguage.addSemanticError("Error : se esperaba un vector de tipo numerico en la función `mode()`.", info);
            }
        } else if (argumentsSize == 2) {
            Expression expression = arguments.get(0);
            Expression trimExpression = arguments.get(1);
            Object resultExp = expression.interpret(aritLanguage, scope);
            if (resultExp instanceof AritVector) {
                AritVector vector = (AritVector) resultExp;
                if (isNumeric(vector)) {
                    Object resultTrim = trimExpression.interpret(aritLanguage, scope);
                    if (resultTrim instanceof AritVector) {
                        AritVector vectorTrim = (AritVector) resultTrim;
                        if (isNumeric(vectorTrim)) {
                            double trim = toNumeric(vectorTrim.getDataNodes().get(0).value);
                            int i;
                            int size = vector.size();
                            double[] values = new double[size];
                            for (i = 0; i < size; i++) {
                                values[i] = toNumeric(vector.getDataNodes().get(i).value);
                            }
                            this.type = TYPE_FACADE.getVectorType();
                            double mode = StatisticalOperations.calculateMode(values, trim);
                            return new AritVector(new DataNode(TYPE_FACADE.getNumericType(), mode));
                        } else {
                            aritLanguage.addSemanticError("Error en trim : se esperaba un vector de tipo numerico en la función `mode()`.", info);
                        }
                    } else {
                        aritLanguage.addSemanticError("Error en trim : se esperaba un vector de tipo numerico en la función `mode()`.", info);
                    }
                } else {
                    aritLanguage.addSemanticError("Error : se esperaba un vector de tipo numerico en la función `mode()`.", info);
                }
            } else{
                aritLanguage.addSemanticError("Error : se esperaba un vector de tipo numerico en la función `mode()`.", info);
            }
        } else {
            aritLanguage.addSemanticError("Error : no se encontró la funcion `mode()` con la cantidad de parametros `" +
                    argumentsSize + "`.", info);
        }
        return null;
    }

    private boolean isNumeric(@NotNull AritVector vector) {
        return TYPE_FACADE.isIntegerType(vector.baseType) || TYPE_FACADE.isNumericType(vector.baseType);
    }

    private double toNumeric(Object value) {
        if (value instanceof Double) return (Double) value;
        if (value instanceof Integer) return ((Integer) value).doubleValue();
        return 0.0;
    }

    public static ModeFunction getInstance() {
        return INSTANCE;
    }

    private static final ModeFunction INSTANCE = new ModeFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
