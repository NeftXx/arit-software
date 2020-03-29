package com.neftxx.interpreter.ast.expression.assignment;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.expression.structure.DataNode;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;

public class Access extends AstNode {
    public enum Type {
        NORMAL,
        TWO_LIST,
        ONE_MATRIX,
        TWO_MATRIX,
        THREE_MATRIX
    }

    public final Expression exp1;
    public final Expression exp2;
    public int[] values = {-1, -1};
    private final Type accessType;

    public Access(NodeInfo info, Expression exp1, Type accessType) {
        super(info);
        this.exp1 = exp1;
        this.exp2 = null;
        this.accessType = accessType;
    }

    public Access(NodeInfo info, Expression exp1, Expression exp2) {
        super(info);
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.accessType = Type.ONE_MATRIX;
    }

    @Override
    public Object interpret(AritLanguage aritLanguage, Scope scope) {
        interpret(aritLanguage, scope, exp1, 0);
        interpret(aritLanguage, scope, exp2, 1);
        return null;
    }

    @Override
    public void createAstGraph(@NotNull StringBuilder astGraph) {
        astGraph.append("\"node").append(this.hashCode()).append("\" [label = \"Acesso\"];\n");
        if (this.exp1 != null) {
            this.exp1.createAstGraph(astGraph);
            astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                    .append(this.exp1.hashCode()).append("\";\n");
        }
        if (this.exp2 != null) {
            this.exp2.createAstGraph(astGraph);
            astGraph.append("\"node").append(this.hashCode()).append("\" -> \"").append("node")
                    .append(this.exp2.hashCode()).append("\";\n");
        }
    }

    private void interpret(AritLanguage aritLanguage, Scope scope, Expression expression, int position) {
        if (expression != null) {
            Object value = expression.interpret(aritLanguage, scope);
            if (value instanceof AritVector) {
                AritVector vector = (AritVector) value;
                if (TYPE_FACADE.isIntegerType(vector.baseType)) {
                    DataNode dataNode = vector.getDataNodes().get(0);
                    this.values[position] = (int) dataNode.value;
                } else {
                    aritLanguage.addSemanticError("Error : la posición debe ser un vector de tipo integer.", this.info);
                }
            } else {
                aritLanguage.addSemanticError("Error : la posición debe ser un vector de tipo integer.", this.info);
            }
        }
    }

    public boolean notAccessVector() {
        return this.accessType != Type.NORMAL;
    }

    public boolean isTypeOneToList() {
        return this.accessType == Type.NORMAL;
    }

    public boolean isTypeTwoToList() {
        return this.accessType == Type.TWO_LIST;
    }

    public boolean isTypeOneToMatrix() {
        return this.accessType == Type.ONE_MATRIX;
    }

    public boolean isTypeTwoToMatrix() {
        return this.accessType == Type.TWO_MATRIX;
    }

    public boolean isTypeThreeToMatrix() {
        return this.accessType == Type.THREE_MATRIX;
    }

    public boolean isTypeFourToMatrix() {
        return this.accessType == Type.NORMAL;
    }

    private String getStringExp(Expression exp) {
        return exp != null ? exp.toString() : "NULL";
    }

    @Override
    public String toString() {
        if (this.isTypeOneToMatrix()) return '[' + getStringExp(this.exp1) + ',' + getStringExp(this.exp2) + ']';
        if (this.isTypeThreeToMatrix()) return '[' + getStringExp(this.exp1) + ",]";
        if (this.isTypeThreeToMatrix()) return "[," + getStringExp(this.exp1) + ']';
        if (this.isTypeTwoToList()) return "[[" + getStringExp(this.exp1) + "]]";
        return "[" + getStringExp(this.exp1) + "]";
    }
}
