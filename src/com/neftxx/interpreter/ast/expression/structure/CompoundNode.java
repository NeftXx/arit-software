package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CompoundNode extends StructureNode {
    public final ArrayList<StructureNode> childrenNodes;

    public CompoundNode(@NotNull ArrayList<StructureNode> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }

    public CompoundNode() {
        this(new ArrayList<>());
    }

    public void initialize(
            int numberDim, int currentIndex, int[] indexes, AritType type, ArrayList<DataNode> dataNodes, AtomicInteger countNodes
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
            CompoundNode child = new CompoundNode();
            child.type = type;
            childrenNodes.add(child);
            child.initialize(numberDim, currentIndex + 1, indexes, type, dataNodes, countNodes);
        }
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
        return new CompoundNode(childrenNodes);
    }

    @Override
    public String toString() {
        StringBuilder cad = new StringBuilder();
        int i, size = size();
        cad.append("[ ");
        for (i = 0; i < size; i++) {
            cad.append(this.childrenNodes.get(i).toString());
            if (i != size - 1) cad.append(", ");
        }
        cad.append(" ]");
        return cad.toString();
    }
}
