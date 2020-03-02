package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AritVector extends AritStructure {
    public AritType baseType;
    private ArrayList<DataNode> dataNodes;

    public AritVector(AritType baseType, Object value) {
        this.baseType = baseType;
        this.dataNodes = new ArrayList<>();
        this.dataNodes.add(new DataNode(baseType, value));
    }

    public AritVector(@NotNull DataNode dataNode) {
        this.baseType = dataNode.type;
        this.dataNodes = new ArrayList<>();
        this.dataNodes.add(dataNode);
    }

    public AritVector(AritType baseType, ArrayList<DataNode> dataNodes) {
        this.baseType = baseType;
        this.dataNodes = dataNodes;
    }

    public void addElement(int position, @NotNull DataNode dataNode) throws IndexOutOfBoundsException {
        if (dataNode.type.priority > this.baseType.priority) {
            AritType oldType = this.baseType;
            AritType newType = dataNode.type;
            Object newValue;
            for (DataNode node: this.dataNodes) {
                newValue = TYPE_FACADE.castValue(oldType, newType, node.value);
                node.changeValues(newType, newValue);
            }
            this.baseType = newType;
        }
        while (position > size()) this.dataNodes.add(DataNode.getDataNodeDefault(this.baseType));
        Object newValue = TYPE_FACADE.castValue(dataNode.type, this.baseType, dataNode.value);
        this.dataNodes.get(position).changeValues(this.baseType, newValue);
    }

    public AritVector getItemAssignment(int position) throws IndexOutOfBoundsException  {
        while (position > size()) this.dataNodes.add(DataNode.getDataNodeDefault(this.baseType));
        return getItemValue(position);
    }

    public AritVector getItemValue(int position) throws IndexOutOfBoundsException  {
        return new AritVector(this.dataNodes.get(position - 1));
    }

    public ArrayList<DataNode> getDataNodes() {
        return this.dataNodes;
    }

    @Override
    public int size() {
        return this.dataNodes.size();
    }

    @Override
    public AritVector copy() {
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        for (DataNode dataNode: this.dataNodes) {
            dataNodes.add(dataNode.copy());
        }
        return new AritVector(this.baseType, dataNodes);
    }

    @Override
    public String toString() {
        StringBuilder cad = new StringBuilder("[ ");
        int i = 0;
        int size = this.size();
        for(; i < size; i++) {
            if (i == size - 1) cad.append(this.dataNodes.get(i)).append(" ");
            else cad.append(this.dataNodes.get(i)).append(", ");
        }
        return cad.append("]").toString();
    }
}
