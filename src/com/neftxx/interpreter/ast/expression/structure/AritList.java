package com.neftxx.interpreter.ast.expression.structure;

import java.util.ArrayList;

public class AritList extends AritStructure  {
    private ArrayList<DataNode> dataNodes;

    public AritList(ArrayList<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    public int size() {
        return this.dataNodes.size();
    }

    @Override
    public AritStructure copy() {
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
        for(; i < size; i++) cad.append(this.dataNodes.get(i)).append(" ");
        return cad.append("}").toString();
    }
}
