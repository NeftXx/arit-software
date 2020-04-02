package com.neftxx.interpreter.ast.expression.function;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritStructure;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FunctionCall extends Expression {
    public final String id;
    public final ArrayList<Expression> arguments;

    public FunctionCall(NodeInfo info, String id, ArrayList<Expression> arguments) {
        super(info);
        this.id = id;
        this.arguments = arguments;
    }

    public FunctionCall(NodeInfo info, String id) {
        this(info, id, null);
    }

    @Override
    public Object interpret(@NotNull AritLanguage aritLanguage, Scope scope) {
        this.type = TYPE_FACADE.getUndefinedType();
        NativeFunction nativeFunction = aritLanguage.globalScope.getNativeFunction(this.id);
        if (nativeFunction != null) {
            this.value = nativeFunction.interpret(this.info, aritLanguage, this.arguments, scope);
            this.type = nativeFunction.type;
            return this.value;
        } else {
            int numberOfArguments = this.arguments != null ? arguments.size() : 0;
            Function function = aritLanguage.globalScope.getMethod(this.id, numberOfArguments, this.info.line);
            if (function != null) {
                int i = 0;
                Expression expression;
                Scope methodScope = new Scope();
                ArrayList<FormalParameter> parameters = function.parameters;
                Object valueArgument;
                for (; i < numberOfArguments; i++) {
                    expression = this.arguments.get(i);
                    valueArgument = expression.interpret(aritLanguage, scope);
                    if (TYPE_FACADE.isUndefinedType(expression.type)) {
                        aritLanguage.addSemanticError("Error : al evaluar el parametro `" +
                                (i + 1) + "`.", this.info);
                        return null;
                    } else if (TYPE_FACADE.isDefaultType(expression.type)) {
                        expression = parameters.get(i).expDefault;
                        if (expression != null) {
                            valueArgument = expression.interpret(aritLanguage, methodScope);
                            if (TYPE_FACADE.isUndefinedType(expression.type)) {
                                aritLanguage.addSemanticError("Error : al evaluar el parametro `" +
                                        (i + 1) + "` por defecto.", this.info);
                                return null;
                            }
                        } else {
                            aritLanguage.addSemanticError("Error : la función `" +
                                            this.id + "` no tiene un valor por defecto en el parametro `" +  (i + 1) + "`.",
                                    this.info);
                            return null;
                        }
                    }
                    if (valueArgument instanceof AritStructure) {
                        valueArgument = ((AritStructure) valueArgument).copy();
                    }
                    methodScope.addVariable(parameters.get(i).id, expression.type, valueArgument, this.info.line);
                }
                methodScope.setPrevious(aritLanguage.globalScope);
                this.value = function.interpret(aritLanguage, methodScope);
                this.type = function.type;
                return this.value;
            } else {
                aritLanguage.addSemanticError("Error : no se encuentra la función `" +
                                this.id + "` con la cantidad de argumentos `" + numberOfArguments + "`.", this.info);
                return null;
            }
        }
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"LlamadaFunción(")
                .append(this.id).append(")\"];\n");
        if (this.arguments != null) {
            for (Expression argument: this.arguments) {
                argument.createAstGraph(astGraph);
                astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                        .append(argument.hashCode()).append("\";\n");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder cad = new StringBuilder(this.id + "(");
        if (this.arguments != null) {
            int i, size = this.arguments.size();
            for (i = 0; i < size; i ++) {
                cad.append(this.arguments.get(i));
                if (i != size - 1) cad.append(", ");
            }
        }
        return cad + ")";
    }
}
