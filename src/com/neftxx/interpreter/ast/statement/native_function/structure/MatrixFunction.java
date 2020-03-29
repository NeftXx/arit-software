package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MatrixFunction extends NativeFunction {
    private MatrixFunction() {
        super("matrix");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, @NotNull ArrayList<Expression> arguments, Scope scope) {
        this.type = TYPE_FACADE.getUndefinedType();
        int argumentsSize = arguments.size();
        if (argumentsSize > 2) {
            Expression vectorExp = arguments.get(0);
            Expression rowsExp = arguments.get(1);
            Expression colExp = arguments.get(2);
            Object resultVector = vectorExp.interpret(aritLanguage, scope);
            Object resultRows = rowsExp.interpret(aritLanguage, scope);
            Object resultCol = colExp.interpret(aritLanguage, scope);
            if (resultVector instanceof AritVector) {
                if (resultRows instanceof AritVector && resultCol instanceof AritVector) {
                    AritVector vectorRows = (AritVector) resultRows;
                    AritVector vectorCol = (AritVector) resultCol;
                    if (TYPE_FACADE.isIntegerType(vectorRows.baseType) && TYPE_FACADE.isIntegerType(vectorCol.baseType)) {
                        AritVector vectorResult = (AritVector) resultVector;
                        DataNode row = vectorRows.getDataNodes().get(0);
                        DataNode col = vectorCol.getDataNodes().get(0);
                        this.type = TYPE_FACADE.getMatrixType();
                        return AritMatrix.createNew(vectorResult.baseType, vectorResult.getDataNodes(),
                                (int) row.value, (int) col.value);
                    } else {
                        aritLanguage.addSemanticError("Error : en la funcion matrix los parametros rows y col" +
                                " deben ser un vector de tipo integer.", info);
                    }
                } else {
                    aritLanguage.addSemanticError("Error : en la funcion matrix los parametros rows y col" +
                            " deben ser un vector de tipo integer.", info);
                }
            } else {
                aritLanguage.addSemanticError("Error : en la funcion el parametro `1` debe ser un vector.", info);
            }
        } else {
            aritLanguage.addSemanticError("Error : no se encontró la funcion matrix con la cantidad de parametros `" +
                    argumentsSize + "`.", info);
        }
        return null;
    }

    public static MatrixFunction getInstance() {
        return INSTANCE;
    }

    private static final MatrixFunction INSTANCE = new MatrixFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
