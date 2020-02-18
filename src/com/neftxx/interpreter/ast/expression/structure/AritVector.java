package com.neftxx.interpreter.ast.expression.structure;

import com.neftxx.interpreter.ast.type.AritType;
import com.neftxx.interpreter.ast.type.TypeFacade;

import java.util.ArrayList;

public class AritVector {
    public AritType type;
    private ArrayList<DataNode> dataNodes;
    public static final TypeFacade TYPE_FACADE = TypeFacade.getInstance();

    public AritVector(AritType type, Object value) {
        if (TYPE_FACADE.isNullType(type)) {
            this.type = TYPE_FACADE.getStringType();
        } else {
            this.type = type;
        }
        this.dataNodes = new ArrayList<>();
        DataNode dataNode = new DataNode(type, value);
        this.dataNodes.add(dataNode);
    }

    public AritVector(AritType type, ArrayList<DataNode> dataNodes) {
        this.type = type;
        this.dataNodes = dataNodes;
    }

    public boolean addElement(int position, AritVector vector) {
        return false;
    }

    public AritVector copy() {
        ArrayList<DataNode> dataNodes = new ArrayList<>();
        for (DataNode dataNode: this.dataNodes) {
            dataNodes.add(dataNode.copy());
        }
        return new AritVector(this.type, dataNodes);
    }
}
