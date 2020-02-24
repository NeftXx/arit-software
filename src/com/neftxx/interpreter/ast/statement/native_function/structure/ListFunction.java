package com.neftxx.interpreter.ast.statement.native_function.structure;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritList;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;

import java.util.ArrayList;

public class ListFunction extends NativeFunction {
    private ListFunction() {
        super("list");
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        this.type = TYPE_FACADE.getUndefinedType();
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        Object value;
        for (Expression argument: arguments) {
            value = argument.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isUndefinedType(argument.type)) {
                this.type = argument.type;
                aritLanguage.addSemanticError("Error : al calcular el valor del parametro de la posición `" +
                        arguments.indexOf(argument) + "` de la función `list()`", argument.info);
                return null;
            }
            dataNodes.add(new DataNode(argument.type, value));
        }
        this.type = TYPE_FACADE.getListType();
        return new AritList(dataNodes);
    }

    public static ListFunction getInstance() {
        return INSTANCE;
    }

    private static final ListFunction INSTANCE = new ListFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
