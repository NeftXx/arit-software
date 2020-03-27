package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AritMatrix extends AritStructure {
    private DataNode[] dataNodes;
    public final int rows;
    public final int columns;

    public AritMatrix(AritType baseType, DataNode[] dataNodes, int rows, int columns) {
        this.baseType = baseType;
        this.dataNodes = dataNodes;
        this.rows = rows;
        this.columns = columns;
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static AritMatrix createNew(AritType baseType, @NotNull ArrayList<DataNode> dataNodes, int rows, int columns) {
        DataNode[] newDataNodes = new DataNode[rows * columns];
        int i, j, currentPosition = 0, size = dataNodes.size() - 1;
        for (i = 0; i < columns; i++) {
            for (j = 0; j < rows; j++) {
                if (currentPosition > size) currentPosition = 0;
                newDataNodes[j * columns + i] = dataNodes.get(currentPosition).copy();
                currentPosition++;
            }
        }
        return new AritMatrix(baseType, newDataNodes, rows, columns);
    }

    public DataNode[] getDataNodes() {
        return dataNodes;
    }

    public ArrayList<DataNode> getAuxDataNodes() {
        int i, j;
        ArrayList<DataNode> auxDataNodes = new ArrayList<>();
        for (i = 0; i < this.columns; i++) {
            for (j = 0; j < this.rows; j++) {
                auxDataNodes.add(this.dataNodes[j * this.columns + i]);
            }
        }
        return auxDataNodes;
    }

    public AritVector modifyItemWithAccessOne(int posX, int posY, @NotNull DataNode dataNode) throws IndexOutOfBoundsException {
        verifyNodeTypeChange(dataNode.baseType);
        Object newValue = dataNode.value;
        if (dataNode.baseType != this.baseType)
            newValue = TYPE_FACADE.castValue(dataNode.baseType, this.baseType, dataNode.value);
        this.dataNodes[(posX - 1) * this.columns + posY - 1].changeValues(this.baseType, newValue);
        return new AritVector(dataNode.copy());
    }

    public AritVector modifyItemWithAccessTwo(AritType newType, int row, @NotNull ArrayList<DataNode> currentDataNodes)
            throws IndexOutOfBoundsException {
        verifyNodeTypeChange(newType);
        row = row - 1;
        int j = 0, currentPosition = 0, size = currentDataNodes.size() - 1;
        ArrayList<DataNode> newDataNodes = new ArrayList<>();
        for (; j < this.columns; j++) {
            currentPosition = changeNodeValue(j, currentDataNodes, row, currentPosition, size, newDataNodes);
        }
        return new AritVector(this.baseType, newDataNodes);
    }

    public AritVector modifyItemWithAccessThree(AritType newType, int column, @NotNull ArrayList<DataNode> currentDataNodes)
            throws IndexOutOfBoundsException {
        verifyNodeTypeChange(newType);
        column = column - 1;
        int i = 0, currentPosition = 0, size = currentDataNodes.size() - 1;
        ArrayList<DataNode> newDataNodes = new ArrayList<>();
        for (; i < this.rows; i++) {
            currentPosition = changeNodeValue(column, currentDataNodes, i, currentPosition, size, newDataNodes);
        }
        return new AritVector(this.baseType, newDataNodes);
    }

    public AritVector modifyItemWithAccessFour(int position, @NotNull DataNode dataNode) throws IndexOutOfBoundsException {
        verifyNodeTypeChange(dataNode.baseType);
        int column, row, count = 0;
        for (column = 0; column < this.columns; column++) {
            for (row = 0; row < this.rows; row++) {
                if (count == position) {
                    this.dataNodes[row * this.columns + column] = dataNode;
                    return new AritVector(dataNode.copy());
                }
                count++;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public AritVector getItemWithAccessOne(int posX, int posY) throws IndexOutOfBoundsException {
        return new AritVector(this.dataNodes[(posX - 1) * this.columns + posY - 1].copy());
    }

    public AritVector getItemWithAccessTwo(int row) throws IndexOutOfBoundsException {
        row = row - 1;
        int j = 0;
        ArrayList<DataNode> newDataNodes = new ArrayList<>();
        for (; j < this.columns; j++) newDataNodes.add(this.dataNodes[row * this.columns + j].copy());
        return new AritVector(this.baseType, newDataNodes);
    }

    public AritVector getItemWithAccessThree(int column) throws IndexOutOfBoundsException {
        column = column - 1;
        int i = 0;
        ArrayList<DataNode> newDataNodes = new ArrayList<>();
        for (; i < this.rows; i++) newDataNodes.add(this.dataNodes[i * this.columns + column].copy());
        return new AritVector(this.baseType, newDataNodes);
    }

    public AritVector getItemWithAccessFour(int position) throws IndexOutOfBoundsException {
        int column , row;
        int count = 0;
        for (column = 0; column < this.columns; column++) {
            for (row = 0; row < this.rows; row++) {
                if (count == position) {
                    return new AritVector(this.dataNodes[row * this.columns + column].copy());
                }
                count++;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public AritVector getItem(int position) throws IndexOutOfBoundsException {
        int column , row;
        int count = 0;
        for (column = 0; column < this.columns; column++) {
            for (row = 0; row < this.rows; row++) {
                if (count == position) {
                    return new AritVector(this.dataNodes[row * this.columns + column]);
                }
                count++;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    private int changeNodeValue(int column, @NotNull ArrayList<DataNode> currentDataNodes, int i, int currentPosition, int size, ArrayList<DataNode> newDataNodes) {
        DataNode dataNode;
        Object newValue;
        if (currentPosition > size) currentPosition = 0;
        dataNode = currentDataNodes.get(currentPosition);
        if (dataNode.baseType != this.baseType)
            newValue = TYPE_FACADE.castValue(dataNode.baseType, this.baseType, dataNode.value);
        else newValue = dataNode.value;
        this.dataNodes[i * this.columns + column].changeValues(this.baseType, newValue);
        newDataNodes.add(currentDataNodes.get(currentPosition).copy());
        currentPosition++;
        return currentPosition;
    }

    private void verifyNodeTypeChange(@NotNull AritType newType) {
        if (newType.priority > this.baseType.priority) {
            AritType oldType = this.baseType;
            Object newValue;
            for (DataNode node : this.dataNodes) {
                newValue = TYPE_FACADE.castValue(oldType, newType, node.value);
                node.changeValues(newType, newValue);
            }
            this.baseType = newType;
        }
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
        return new AritMatrix(this.baseType, dataNodes, this.rows, this.columns);
    }

    @Override
    public String toString() {
        int i, j;
        StringBuilder cad = new StringBuilder("\n  | ");
        for (i = 0; i < this.rows; i++) {
            for (j = 0; j < this.columns; j++) {
                cad.append(this.dataNodes[i * this.columns + j]).append(" | ");
            }
            if (i != this.rows - 1) cad.append('\n').append("  | ");
        }
        return cad.toString();
    }
}
