package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CompoundNode extends StructureNode {
    public final ArrayList<StructureNode> childrenNodes;
    public AritType principalType;

    public CompoundNode(@NotNull AritType principalType, @NotNull AritType baseType, @NotNull ArrayList<StructureNode> childrenNodes) {
        this.principalType = principalType;
        this.baseType = baseType;
        this.childrenNodes = childrenNodes;
    }

    public CompoundNode(@NotNull AritType principalType, @NotNull AritType baseType) {
        this(principalType, baseType, new ArrayList<>());
    }

    public void initialize(
            int numberDim, int currentIndex, int[] indexes, ArrayList<DataNode> dataNodes, AtomicInteger countNodes
    ) {
        int i;
        if (currentIndex == numberDim) {
            for (i = 0; i < indexes[currentIndex - 1]; i++) {
                if (countNodes.get() == dataNodes.size()) countNodes.set(0);
                childrenNodes.add(dataNodes.get(countNodes.get()).copy());
                countNodes.set(countNodes.get() + 1);
            }
            return;
        }
        for (i = 0; i < indexes[currentIndex - 1]; i++) {
            CompoundNode child = new CompoundNode(this.principalType, this.baseType);
            childrenNodes.add(child);
            child.initialize(numberDim, currentIndex + 1, indexes, dataNodes, countNodes);
        }
    }

    public void changeTypes(int numberDim, int currentIndex, int[] indexes, AritType newType) {
        int i;
        if (currentIndex == numberDim) {
            for (i = 0; i < indexes[currentIndex - 1]; i++) {
                DataNode node = (DataNode) childrenNodes.get(i);
                AritVector vector = (AritVector) node.value;
                node = vector.getDataNodes().get(0);
                Object value = node.value;
                while (value instanceof DataNode) {
                    value = ((DataNode) value).value;
                }
                if (TYPE_FACADE.isBaseType(newType)) {
                    AritType oldType = vector.baseType;
                    Object newValue = TYPE_FACADE.castValue(oldType, newType, value);
                    vector.baseType = newType;
                    node.changeValues(newType, newValue);
                    this.baseType = newType;
                } else {
                    AritList list = new AritList(vector);
                    this.principalType = TYPE_FACADE.getListType();
                    this.baseType = this.principalType;
                    DataNode temp = (DataNode) childrenNodes.get(i);
                    temp.changeValues(this.baseType, list);
                }
            }
            return;
        }
        for (i = 0; i < indexes[currentIndex - 1]; i++) {
            CompoundNode child = (CompoundNode) childrenNodes.get(i);
            if (TYPE_FACADE.isBaseType(newType)) {
                child.baseType = newType;
            }
            else {
                child.principalType = TYPE_FACADE.getListType();
                child.baseType = child.principalType;
            }
            child.changeTypes(numberDim, currentIndex + 1, indexes, newType);
        }
    }

    @Override
    public void setValue(int position, @NotNull int[] indexes, Object value) throws IndexOutOfBoundsException {
        StructureNode child = childrenNodes.get(indexes[position]);
        child.setValue(position + 1, indexes, value);
    }

    public ArrayList<DataNode> getDataNodes() {
        int i, size = size();
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        StructureNode structureNode;
        for (i = 0; i < size; i++) {
            structureNode = this.childrenNodes.get(i);
            if (structureNode instanceof CompoundNode) {
                dataNodes.addAll(((CompoundNode) structureNode).getDataNodes());
            } else {
                dataNodes.add((DataNode) structureNode);
            }
        }
        return dataNodes;
    }

    public StructureNode get(int index) {
        return this.childrenNodes.get(index);
    }

    @Override
    public int size() {
        return this.childrenNodes.size();
    }

    @Override
    public CompoundNode copy() {
        int i, size = size();
        ArrayList<StructureNode> childrenNodes = new ArrayList<>();
        for (i = 0; i < size; i++) {
            childrenNodes.add(this.childrenNodes.get(i).copy());
        }
        return new CompoundNode(this.principalType, this.baseType, childrenNodes);
    }

    @Override
    public Object getValue(int position, @NotNull int[] indexes) throws IndexOutOfBoundsException {
        StructureNode child = this.childrenNodes.get(indexes[position]);
        return child.getValue(position + 1, indexes);
    }

    @Override
    public String toString() {
        return childrenNodes.toString();
    }
}
