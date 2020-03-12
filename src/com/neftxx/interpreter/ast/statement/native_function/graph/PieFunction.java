package com.neftxx.interpreter.ast.statement.native_function.graph;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PieFunction extends NativeFunction {
    private PieFunction() {
        super("pie");
        this.type = TYPE_FACADE.getUndefinedType();
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        int numberOfArguments = arguments.size();
        if (numberOfArguments == 3) {
            Expression xExp = arguments.get(0);
            Expression labelsExp = arguments.get(1);
            Expression mainExp = arguments.get(2);
            Object resultXExp = xExp.interpret(aritLanguage, scope);
            Object resultLabelsExp = labelsExp.interpret(aritLanguage, scope);
            Object resultMainExp = mainExp.interpret(aritLanguage, scope);
            if (TYPE_FACADE.isVectorType(xExp.type) && TYPE_FACADE.isVectorType(labelsExp.type) &&
                    TYPE_FACADE.isVectorType(mainExp.type)) {
                AritVector xAritVector = (AritVector) resultXExp;
                AritVector labelsAritVector = (AritVector) resultLabelsExp;
                AritVector mainAritVector = (AritVector) resultMainExp;
                if ((TYPE_FACADE.isIntegerType(xAritVector.baseType) || TYPE_FACADE.isNumericType(xAritVector.baseType))
                        && TYPE_FACADE.isStringType(labelsAritVector.baseType)
                        && TYPE_FACADE.isStringType(mainAritVector.baseType)) {
                    int i, sizeX = xAritVector.size(), sizeLabels = labelsAritVector.size();
                    if (sizeX != sizeLabels) {

                    }
                    double[] values = new double[sizeX];
                    String[] labels = new String[sizeX];
                    int count = 1;
                    for (i = 0; i < sizeX; i++) {
                        values[i] = toDouble(xAritVector.getDataNodes().get(i).value);
                        if (i >= sizeLabels) {
                            labels[i] = "Desconocido " + count;
                            count++;
                        } else {
                            labels[i] = toString(labelsAritVector.getDataNodes().get(i).value);
                        }
                    }
                    String title = toString(mainAritVector.getDataNodes().get(0).value);
                    aritLanguage.addChart(getChart(values, labels, title));
                } else {

                }
            } else {

            }
        } else {
        }
        return null;
    }

    @NotNull
    @Contract(pure = true)
    private PieChart getChart(@NotNull double[] values, String[] labels, String title) {
        int i, size = values.length;
        ArrayList<PieChart.Data> list = new ArrayList<>();
        for (i = 0; i < size; i++) list.add(new PieChart.Data(labels[i], values[i]));
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(list);
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(title);
        pieChart.setClockwise(true);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);
        return pieChart;
    }

    public static PieFunction getInstance() {
        return INSTANCE;
    }

    private double toDouble(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Double) return (Double) value;
        return 0;
    }

    private String toString(Object value) {
        return value != null ? value.toString() : "NULL";
    }

    private static final PieFunction INSTANCE = new PieFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
