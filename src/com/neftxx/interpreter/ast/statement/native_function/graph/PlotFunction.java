package com.neftxx.interpreter.ast.statement.native_function.graph;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import javafx.scene.chart.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlotFunction extends NativeFunction {
    private PlotFunction() {
        super("plot");
    }

    @Override
    public Object interpret(
            NodeInfo info, AritLanguage aritLanguage, @NotNull ArrayList<Expression> arguments, Scope scope
    ) {
        int numberOfArguments = arguments.size();
        if (numberOfArguments == 5) {
            Expression expArgument1 = arguments.get(0), expArgument2 = arguments.get(1), expArgument3 = arguments.get(2),
                    expArgument4 = arguments.get(3), expArgument5 = arguments.get(4);
            Object resultArgument1 = expArgument1.interpret(aritLanguage, scope),
                    resultArgument2 = expArgument2.interpret(aritLanguage, scope),
                    resultArgument3 = expArgument3.interpret(aritLanguage, scope),
                    resultArgument4 = expArgument4.interpret(aritLanguage, scope),
                    resultArgument5 = expArgument5.interpret(aritLanguage, scope);
            if (isVector(expArgument1)) {
                if (isVector(expArgument2) && isVector(expArgument3) && isVector(expArgument4) && isVector(expArgument5)) {
                    AritVector vectorArgument1 = (AritVector) resultArgument1,
                            vectorArgument2 = (AritVector) resultArgument2,
                            vectorArgument3 = (AritVector) resultArgument3,
                            vectorArgument4 = (AritVector) resultArgument4,
                            vectorArgument5 = (AritVector) resultArgument5;
                    if (isNumber(vectorArgument1) && isString(vectorArgument2) && isString(vectorArgument3) &&
                            isString(vectorArgument4) && isString(vectorArgument5)) {
                        int i, sizeValues = vectorArgument1.size();
                        double[] values = new double[sizeValues];
                        for (i = 0; i < sizeValues; i++) {
                            values[i] = toDouble(vectorArgument1.getDataNodes().get(i).value);
                        }
                        String type = toString(vectorArgument2.getDataNodes().get(0).value);
                        if (!(type.equalsIgnoreCase("P") ||
                                type.equalsIgnoreCase("I") ||
                                type.equalsIgnoreCase("O"))) {
                            // error
                            type = "O";
                        }
                        String labelX = toString(vectorArgument3.getDataNodes().get(0).value);
                        String labelY = toString(vectorArgument4.getDataNodes().get(0).value);
                        String title = toString(vectorArgument5.getDataNodes().get(0).value);
                        aritLanguage.addChart(getLineChart(values, type, labelX, labelY, title));
                    } else if (isNumber(vectorArgument1) && isString(vectorArgument2) && isString(vectorArgument3) &&
                            isString(vectorArgument4) && isNumber(vectorArgument5)) {
                        int i, sizeValues = vectorArgument1.size();
                        double[] values = new double[sizeValues];
                        for (i = 0; i < sizeValues; i++) {
                            values[i] = toDouble(vectorArgument1.getDataNodes().get(i).value);
                        }
                        String labelX = toString(vectorArgument2.getDataNodes().get(0).value);
                        String labelY = toString(vectorArgument3.getDataNodes().get(0).value);
                        String title = toString(vectorArgument4.getDataNodes().get(0).value);
                        int sizeLim  = vectorArgument5.size();
                        double min = 0, max = 0;
                        if (sizeLim >= 2) {
                            min = toDouble(vectorArgument5.getDataNodes().get(0).value);
                            max = toDouble(vectorArgument5.getDataNodes().get(1).value);
                        }
                        aritLanguage.addChart(getScatterChart(values, labelX, labelY, title, min, max));
                    } else {
                        // error
                    }
                } else {}
            } else if (isMatrix(expArgument1)) {
                if (isVector(expArgument2) && isVector(expArgument3) && isVector(expArgument4) && isVector(expArgument5)) {
                    AritMatrix matrixArgument1 = (AritMatrix) resultArgument1;
                    AritVector vectorArgument2 = (AritVector) resultArgument2,
                            vectorArgument3 = (AritVector) resultArgument3,
                            vectorArgument4 = (AritVector) resultArgument4,
                            vectorArgument5 = (AritVector) resultArgument5;
                    if (isNumber(matrixArgument1) && isString(vectorArgument2) && isString(vectorArgument3) &&
                            isString(vectorArgument4) && isString(vectorArgument5)) {
                        int i, sizeValues = matrixArgument1.size();
                        double[] values = new double[sizeValues];
                        for (i = 0; i < sizeValues; i++) {
                            values[i] = toDouble(matrixArgument1.getDataNodes()[i].value);
                        }
                        String type = toString(vectorArgument2.getDataNodes().get(0).value);
                        if (!(type.equalsIgnoreCase("P") ||
                                type.equalsIgnoreCase("I") ||
                                type.equalsIgnoreCase("O"))) {
                            // error
                            type = "O";
                        }
                        String labelX = toString(vectorArgument3.getDataNodes().get(0).value);
                        String labelY = toString(vectorArgument4.getDataNodes().get(0).value);
                        String title = toString(vectorArgument5.getDataNodes().get(0).value);
                        aritLanguage.addChart(getLineChart(values, type, labelX, labelY, title));
                    } else if (isNumber(matrixArgument1) && isString(vectorArgument2) && isString(vectorArgument3) &&
                            isString(vectorArgument4) && isNumber(vectorArgument5)) {
                        int i, sizeValues = matrixArgument1.size();
                        double[] values = new double[sizeValues];
                        for (i = 0; i < sizeValues; i++) {
                            values[i] = toDouble(matrixArgument1.getDataNodes()[i].value);
                        }
                        String labelX = toString(vectorArgument2.getDataNodes().get(0).value);
                        String labelY = toString(vectorArgument3.getDataNodes().get(0).value);
                        String title = toString(vectorArgument4.getDataNodes().get(0).value);
                        int sizeLim  = vectorArgument5.size();
                        double min = 0, max = 0;
                        if (sizeLim >= 2) {
                            min = toDouble(vectorArgument5.getDataNodes().get(0).value);
                            max = toDouble(vectorArgument5.getDataNodes().get(1).value);
                        }
                        aritLanguage.addChart(getScatterChart(values, labelX, labelY, title, min, max));
                    } else {
                        // error
                    }
                } else {}
            } else {
                // error
            }
        } else {
            // error
        }
        return null;
    }

    @NotNull
    private LineChart<Number, Number> getLineChart(
            double[] values, @NotNull String type, String labelX, String labelY, String title
    ) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(labelX);
        xAxis.setStyle("-fx-font-size: 16px;");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(labelY);
        yAxis.setStyle("-fx-font-size: 16px;");
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        XYChart.Series<Number, Number> series = getSeries(values);
        chart.getData().add(series);
        chart.setStyle("-fx-background-color: white; -fx-font-weight: bold");
        if (type.equalsIgnoreCase("P")) {
            series.getNode().setStyle("-fx-stroke: transparent;");
        } else if (type.equalsIgnoreCase("I")) chart.setCreateSymbols(false);
        return chart;
    }

    @NotNull
    private ScatterChart<Number, Number> getScatterChart(
            double[] values, String labelX, String labelY, String title, double min, double max
    ) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(labelX);
        xAxis.setStyle("-fx-font-size: 16px;");
        NumberAxis yAxis;
        if (max > min) yAxis = new NumberAxis(min, max, 1);
        else yAxis = new NumberAxis();
        yAxis.setLabel(labelY);
        yAxis.setStyle("-fx-font-size: 16px;");
        ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.getData().add(getSeries(values));
        chart.setStyle("-fx-background-color: white; -fx-font-weight: bold;");
        return chart;
    }

    @NotNull
    private XYChart.Series<Number, Number> getSeries(@NotNull double[] values) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Datos");
        int i, length = values.length;
        for (i = 0; i < length; i++) {
            series.getData().add(new XYChart.Data<>(i + 1, values[i]));
        }
        return series;
    }

    private boolean isNumber(@NotNull AritVector vector) {
        return TYPE_FACADE.isIntegerType(vector.baseType) || TYPE_FACADE.isNumericType(vector.baseType);
    }

    private boolean isNumber(@NotNull AritMatrix matrix) {
        return TYPE_FACADE.isIntegerType(matrix.baseType) || TYPE_FACADE.isNumericType(matrix.baseType);
    }

    private boolean isString(@NotNull AritVector vector) {
        return TYPE_FACADE.isStringType(vector.baseType);
    }

    private double toDouble(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Double) return (Double) value;
        return 0;
    }

    private String toString(Object value) {
        return value != null ? value.toString() : "NULL";
    }

    private boolean isVector(@NotNull Expression exp) {
        return TYPE_FACADE.isVectorType(exp.type);
    }

    private boolean isMatrix(@NotNull Expression exp) {
        return TYPE_FACADE.isMatrixType(exp.type);
    }

    public static PlotFunction getInstance() {
        return INSTANCE;
    }

    private static final PlotFunction INSTANCE = new PlotFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
