package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AritList extends AritStructure  {
    private ArrayList<DataNode> dataNodes;

    public AritList(DataNode dataNode) {
        this.dataNodes = new ArrayList<>();
        this.dataNodes.add(dataNode);
    }

    public AritList(ArrayList<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    public AritList(@NotNull AritVector vector) {
        this.dataNodes = new ArrayList<>();
        for (DataNode currentNode: vector.getDataNodes()) {
            if (TYPE_FACADE.isBaseType(currentNode.baseType)) {
                AritType currentType = currentNode.baseType;
                Object currentValue = currentNode.value;
                this.dataNodes.add(new DataNode(TYPE_FACADE.getVectorType(), new AritVector(currentType, currentValue)));
            }
        }
    }

    @Override
    public int size() {
        return this.dataNodes.size();
    }

    public void addElement(int position, @NotNull DataNode dataNode) throws IndexOutOfBoundsException {
        addElement(position, dataNode.baseType, dataNode.value);
    }

    public void addElement(int position, AritType type, Object value) throws IndexOutOfBoundsException {
        while (position > size()) {
            this.dataNodes.add(
                    new DataNode(
                            TYPE_FACADE.getVectorType(),
                            new AritVector(DataNode.getDataNodeDefault(TYPE_FACADE.getStringType()))
                    )
            );
        }
        this.dataNodes.get(position - 1).changeValues(type, value);
    }

    public DataNode getItemValue(int position) throws IndexOutOfBoundsException  {
        return this.dataNodes.get(position - 1);
    }

    public DataNode getItemAssignment(int position) throws IndexOutOfBoundsException  {
        while (position > size()) {
            this.dataNodes.add(
                    new DataNode(
                            TYPE_FACADE.getVectorType(),
                            new AritVector(DataNode.getDataNodeDefault(TYPE_FACADE.getStringType()))
                    )
            );
        }
        return getItemValue(position);
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
