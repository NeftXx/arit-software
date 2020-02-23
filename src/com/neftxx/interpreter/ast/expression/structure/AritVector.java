package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AritVector extends AritStructure {
    public AritType type;
    private ArrayList<DataNode> dataNodes;

    public AritVector(AritType type, Object value) {
        this.type = type;
        this.dataNodes = new ArrayList<>();
        this.dataNodes.add(new DataNode(type, value));
    }

    public AritVector(@NotNull DataNode dataNode) {
        this.type = dataNode.type;
        this.dataNodes = new ArrayList<>();
        this.dataNodes.add(dataNode);
    }

    public AritVector(AritType type, ArrayList<DataNode> dataNodes) {
        this.type = type;
        this.dataNodes = dataNodes;
    }

    public void addElement(int position, @NotNull DataNode dataNode) throws IndexOutOfBoundsException {
        if (dataNode.type.priority > this.type.priority) {
            AritType oldType = this.type;
            AritType newType = dataNode.type;
            Object newValue;
            for (DataNode node: this.dataNodes) {
                newValue = TYPE_FACADE.castValue(oldType, newType, node.value);
                node.changeValues(newType, newValue);
            }
            this.type = newType;
        }
        while (position > size()) this.dataNodes.add(DataNode.getDataNodeDefault(this.type));
        Object newValue = TYPE_FACADE.castValue(dataNode.type, this.type, dataNode.value);
        this.dataNodes.get(position).changeValues(this.type, newValue);
    }

    public AritVector getElement(int position) throws IndexOutOfBoundsException  {
        return new AritVector(this.dataNodes.get(position));
    }

    public ArrayList<DataNode> getDataNodes() {
        return this.dataNodes;
    }

    public int size() {
        return this.dataNodes.size();
    }

    @Override
    public AritVector copy() {
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        for (DataNode dataNode: this.dataNodes) {
            dataNodes.add(dataNode.copy());
        }
        return new AritVector(this.type, dataNodes);
    }

    @Override
    public String toString() {
        return "Vector(Tipo: "+ this.type +") -> " + this.dataNodes;
    }
}
