package com.neftxx.interpreter.ast.expression.structure;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AritMatrix extends AritStructure {
    private DataNode[] dataNodes;
    public final int rows;
    public final int columns;

    public AritMatrix(DataNode[] dataNodes, int rows, int columns) {
        this.dataNodes = dataNodes;
        this.rows = rows;
        this.columns = columns;
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static AritMatrix createNew(@NotNull ArrayList<DataNode> dataNodes, int rows, int columns) {
        int size = dataNodes.size();
        DataNode[] newDataNodes = new DataNode[rows * columns];
        int i, j, currentPosition = 0;
        for (i = 0; i < columns; i++) {
            for (j = 0; j < rows; j++) {
                if (currentPosition > size - 1) currentPosition = 0;
                newDataNodes[j * rows + i]  = dataNodes.get(currentPosition).copy();
                currentPosition++;
            }
        }
        return new AritMatrix(newDataNodes, rows, columns);
    }

    public DataNode[] getDataNodes() {
        return dataNodes;
    }

    @Override
    public int size() {
        return this.dataNodes.length;
    }

    @Override
    public AritMatrix copy() {
        int i = 0;
        int length = this.dataNodes.length;
        DataNode[] dataNodes = new DataNode[length];
        for (; i < length; i++) {
            dataNodes[i] = this.dataNodes[i].copy();
        }
        return new AritMatrix(dataNodes, this.rows, this.columns);
    }

    @Override
    public String toString() {
        int i, j;
        StringBuilder cad = new StringBuilder();
        for (i = 0; i < this.columns; i++) {
            for (j = 0; j < this.rows; j++) {
                cad.append(this.dataNodes[j * this.rows + i]).append(" ");
            }
            if (i != this.columns - 1) cad.append('\n').append("  ");
        }
        return cad.toString();
    }
}
