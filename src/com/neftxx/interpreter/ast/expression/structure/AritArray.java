package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AritArray extends AritStructure {
    private int size;
    public AritType principalType;
    public AritType auxType;
    public CompoundNode root;
    public int[] indexes;

    public AritArray(@NotNull AritType principalType, @NotNull AritType auxType, @NotNull CompoundNode root, @NotNull int[] indexes) {
        this.principalType = principalType;
        this.auxType = auxType;
        this.root = root;
        this.indexes = indexes;
        int i, length = indexes.length, result = 1;
        for (i = 0; i < length; i++) result *= indexes[i];
        this.size = result;
    }

    public ArrayList<DataNode> getDataNodes() {
        return new ArrayList<>(this.root.getDataNodes());
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    public static AritArray createNewArray(
            ArrayList<DataNode> dataNodes, int numberDim, int[] indexes, AritType principalType, AritType auxType
    ) {
        CompoundNode root = new CompoundNode(principalType, auxType);
        root.initialize(numberDim, 1, indexes, dataNodes, new AtomicInteger(0));
        return new AritArray(principalType, auxType, root, indexes);
    }

    public Object getValue(int position, int[] indexes) {
        return this.root.getValue(position, indexes);
    }

    public void setValueVector(int position, int[] indexes, @NotNull AritType newType, Object value) throws IndexOutOfBoundsException {
        if (newType.priority > this.auxType.priority) {
            if (TYPE_FACADE.isListType(newType)) {
                this.principalType = this.baseType = newType;
            }
            this.root.changeTypes(numberOfDimensions(), 1, this.indexes, newType);
        }
        this.root.setValue(position, indexes, value);
    }

    public void setValueList(int position, int[] indexes, Object value) throws IndexOutOfBoundsException {
        this.root.setValue(position, indexes, value);
    }

    @Override
    public int size() {
        return this.size;
    }

    public int numberOfDimensions() {
        return this.indexes.length;
    }

    @Override
    public AritArray copy() {
        return new AritArray(this.principalType, this.auxType, this.root.copy(), this.indexes);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        generateText(stringBuilder);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return '\n' + stringBuilder.toString();
    }

    private void generateText(StringBuilder stringBuilder) {
        int dimensions = this.indexes.length;
        if (dimensions > 1) {
            generateText(stringBuilder, 0, "", this.root);
        } else {
            int i = 0;
            int size = this.root.size();
            stringBuilder.append("   |   ");
            for (; i < size; i++) {
                if (i == size - 1) stringBuilder.append(this.root.get(i)).append(" ");
                else stringBuilder.append(this.root.get(i)).append(", ");
            }
            stringBuilder.append("   |   ");
        }
    }

    private void generateText(StringBuilder stringBuilder, int currentDimension, String head, CompoundNode compoundNode) {
        if (this.indexes.length - currentDimension == 2) {
            if (!head.isEmpty()) {
                head = ", " + head;
                stringBuilder.append(head).append('\n');
            }
            int i, j;
            int rows = this.indexes[currentDimension + 1];
            int columns = this.indexes[currentDimension];
            CompoundNode temp;
            for (i = 0; i < rows; i++) {
                stringBuilder.append("   |   ");
                for (j = 0; j < columns; j++) {
                    temp = (CompoundNode) compoundNode.get(j);
                    stringBuilder.append(temp.get(i)).append("   |   ");
                }
                stringBuilder.append("\n");
            }
        } else {
            int index = this.indexes[currentDimension];
            int i = 0;
            String headTemp;
            for (; i < index; i++) {
                headTemp = head;
                head = "," + (i + 1) + head;
                generateText(stringBuilder, currentDimension + 1, head, (CompoundNode) compoundNode.get(i));
                head = headTemp;
            }
        }
    }
}
