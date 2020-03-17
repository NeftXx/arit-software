package com.neftxx.interpreter.ast.statement.native_function.graph;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritMatrix;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class HistFunction extends NativeFunction {
    private HistFunction() {
        super("hist");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        int numberOfArguments = arguments.size();
        if (numberOfArguments == 3) {
            Expression expArgument1 = arguments.get(0), expArgument2 = arguments.get(1), expArgument3 = arguments.get(2);
            Object resArgument1 = expArgument1.interpret(aritLanguage, scope),
                    resArgument2 = expArgument2.interpret(aritLanguage, scope),
                    resArgument3 = expArgument3.interpret(aritLanguage, scope);
            if (isVector(expArgument1) && isVector(expArgument2) && isVector(expArgument3)) {
                AritVector vectorArg1 = (AritVector) resArgument1,
                        vectorArg2 = (AritVector) resArgument2,
                        vectorArg3 = (AritVector) resArgument3;
                if (isNumber(vectorArg1) && isString(vectorArg2) && isString(vectorArg3)) {
                    int i, sizeValues = vectorArg1.size();
                    double[] values = new double[sizeValues];
                    for (i = 0; i < sizeValues; i++) {
                        values[i] = toDouble(vectorArg1.getDataNodes().get(i).value);
                    }
                    String xLab = toString(vectorArg2.getDataNodes().get(0).value);
                    String title = toString(vectorArg3.getDataNodes().get(0).value);
                    aritLanguage.addChart(getChartViewer(values, xLab, title));
                } else {
                    // TODO: AGREGAR ERROR
                }
            } else if (isMatrix(expArgument1) && isVector(expArgument2) && isVector(expArgument3)) {
                AritMatrix matrixArg1 = (AritMatrix) resArgument1;
                AritVector vectorArg2 = (AritVector) resArgument2,
                        vectorArg3 = (AritVector) resArgument3;
                if (isNumber(matrixArg1) && isString(vectorArg2) && isString(vectorArg3)) {
                    int i, sizeValues = matrixArg1.size();
                    double[] values = new double[sizeValues];
                    for (i = 0; i < sizeValues; i++) {
                        values[i] = toDouble(matrixArg1.getDataNodes()[i].value);
                    }
                    String xLab = toString(vectorArg2.getDataNodes().get(0).value);
                    String title = toString(vectorArg3.getDataNodes().get(0).value);
                    aritLanguage.addChart(getChartViewer(values, xLab, title));
                } else {
                    // TODO: AGREGAR ERROR
                }
            } else {
                // TODO: AGREGAR ERROR
            }
        } else {
            // TODO: AGREGAR ERROR
        }
        return null;
    }

    public ChartViewer getChartViewer(@NotNull double[] values, String xLab, String title) {
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        dataset.addSeries("Datos", values, 10);
        JFreeChart histogram = ChartFactory.createHistogram(title, xLab, "", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        ChartViewer chartViewer = new ChartViewer(histogram);
        chartViewer.setMinWidth(455);
        chartViewer.setMinHeight(405);
        return chartViewer;
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

    public static HistFunction getInstance() {
        return INSTANCE;
    }

    private static final HistFunction INSTANCE = new HistFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
