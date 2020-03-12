package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AritArray extends AritStructure {
    private int size;
    public AritType baseType;
    public CompoundNode root;
    public int[] indexes;

    public AritArray(@NotNull AritType baseType, @NotNull CompoundNode root, @NotNull int[] indexes) {
        this.baseType = baseType;
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
    @Contract("_, _, _, _ -> new")
    public static AritArray createNewArray(ArrayList<DataNode> dataNodes, int numberDim, int[] indexes, AritType type) {
        CompoundNode root = new CompoundNode();
        root.type = type;
        root.initialize(numberDim, 1, indexes, type, dataNodes, new AtomicInteger(0));
        return new AritArray(type, root, indexes);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public AritArray copy() {
        return new AritArray(this.baseType, this.root.copy(), this.indexes);
    }

    @Override
    public String toString() {
        return this.root.toString();
    }
}
