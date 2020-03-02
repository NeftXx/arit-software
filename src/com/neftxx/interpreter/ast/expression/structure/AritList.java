package com.neftxx.interpreter.ast.expression.structure;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AritList extends AritStructure  {
    private ArrayList<DataNode> dataNodes;

    public AritList(ArrayList<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    public AritList(@NotNull AritVector vector) {
        this.dataNodes = vector.getDataNodes();
    }

    @Override
    public int size() {
        return this.dataNodes.size();
    }

    public ArrayList<DataNode> getDataNodes() {
        return this.dataNodes;
    }

    @Override
    public AritList copy() {
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        for (DataNode dataNode: this.dataNodes) {
            dataNodes.add(dataNode.copy());
        }
        return new AritList(dataNodes);
    }

    @Override
    public String toString() {
        StringBuilder cad = new StringBuilder("{ ");
        int i = 0;
        int size = this.size();
        for(; i < size; i++) {
            if (i == size - 1) cad.append(this.dataNodes.get(i)).append(" ");
            else cad.append(this.dataNodes.get(i)).append(", ");
        }
        return cad.append("}").toString();
    }
}
