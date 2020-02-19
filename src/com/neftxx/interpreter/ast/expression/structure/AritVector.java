package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.interpreter.ast.type.TypeFacade;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AritVector {
    public AritType type;
    private ArrayList<DataNode> dataNodes;

    public AritVector(AritType type, Object value) {
        this.dataNodes = new ArrayList<>();
        DataNode dataNode = new DataNode(type, value);
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

    public DataNode getElement(int position) throws IndexOutOfBoundsException  {
        return this.dataNodes.get(position);
    }

    public int size() {
        return this.dataNodes.size();
    }

    public AritVector copy() {
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        for (DataNode dataNode: this.dataNodes) {
            dataNodes.add(dataNode.copy());
        }
        return new AritVector(this.type, dataNodes);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[1] ");
        for (DataNode dataNode : this.dataNodes) {
            builder.append(dataNode.toString()).append(" ");
        }
        return builder.toString();
    }

    private static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();
}
