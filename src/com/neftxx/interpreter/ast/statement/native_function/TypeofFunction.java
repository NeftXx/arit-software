package com.neftxx.interpreter.ast.statement.native_function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritArray;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TypeofFunction extends NativeFunction {
    private TypeofFunction() {
        super("typeof");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, @NotNull ArrayList<Expression> arguments, Scope scope) {
        int size = arguments.size();
        if (size == 1) {
            Expression argument = arguments.get(0);
            Object value = argument.interpret(aritLanguage, scope);
            this.type = TYPE_FACADE.getVectorType();
            if (value instanceof AritVector) {
                return new AritVector(TYPE_FACADE.getStringType(), ((AritVector) value).baseType.toString());
            }

            if (value instanceof AritMatrix) {
                return new AritVector(TYPE_FACADE.getStringType(), ((AritMatrix) value).baseType.toString());
            }

            if (value instanceof AritArray) {
                return new AritVector(TYPE_FACADE.getStringType(), ((AritArray) value).auxType.toString());
            }

            return new AritVector(TYPE_FACADE.getStringType(), argument.type.toString());
        } else {
            aritLanguage.addSemanticError("Error : el tamaño `" + size +
                    "` de parámetros no es válido para la función typeof().", info);
        }
        this.type = TYPE_FACADE.getUndefinedType();
        return null;
    }

    public static TypeofFunction getInstance() {
        return INSTANCE;
    }

    private static final TypeofFunction INSTANCE = new TypeofFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
