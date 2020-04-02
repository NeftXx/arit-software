package com.neftxx.interpreter.ast.statement.native_function.graph;

import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.expression.Expression;
import com.neftxx.interpreter.ast.expression.structure.AritVector;
import com.neftxx.interpreter.ast.scope.Scope;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import com.neftxx.util.NodeInfo;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BarPlotFunction extends NativeFunction {
    private BarPlotFunction() {
        super("barplot");
    }

    @Override
    public Object interpret(NodeInfo info, AritLanguage aritLanguage, ArrayList<Expression> arguments, Scope scope) {
        int numberOfArguments = arguments.size();
        if (numberOfArguments == 5) {
            Expression expH = arguments.get(0), expXLab = arguments.get(1), expYLab = arguments.get(2),
                    expMain = arguments.get(3), expNamesArg = arguments.get(4);
            Object resultH = expH.interpret(aritLanguage, scope), resultXLab = expXLab.interpret(aritLanguage, scope),
                    resultYLab = expYLab.interpret(aritLanguage, scope), resultMain = expMain.interpret(aritLanguage, scope),
                    resultNamesArg = expNamesArg.interpret(aritLanguage, scope);
            if (isVector(resultH) && isVector(resultXLab) && isVector(resultYLab) && isVector(resultMain) && isVector(resultNamesArg)) {
                AritVector vectorH = (AritVector) resultH, vectorXLab = (AritVector) resultXLab,
                        vectorYLab = (AritVector) resultYLab, vectorMain = (AritVector) resultMain,
                        vectorNamesArg = (AritVector) resultNamesArg;
                if (isNumber(vectorH) && isString(vectorXLab) && isString(vectorYLab) && isString(vectorMain)
                        && isString(vectorNamesArg)) {
                    int i, sizeH = vectorH.size(), sizeNames = vectorNamesArg.size();
                    if (sizeH != sizeNames) {
                        aritLanguage.addSemanticError("Warning : la cantidad de nombres deben ser iguales " +
                                "a los datos.", info);
                    }
                    double[] values = new double[sizeH];
                    String[] names = new String[sizeH];
                    int count = 1;
                    for (i = 0; i < sizeH; i++) {
                        values[i] = toDouble(vectorH.getDataNodes().get(i).value);
                        if (i >= sizeNames) {
                            names[i] = "Desconocido " + count;
                            count++;
                        } else {
                            names[i] = toString(vectorNamesArg.getDataNodes().get(i).value);
                        }
                    }
                    String xLab = toString(vectorXLab.getDataNodes().get(0).value);
                    String yLab = toString(vectorYLab.getDataNodes().get(0).value);
                    String title = toString(vectorMain.getDataNodes().get(0).value);
                    aritLanguage.addChart(getChart(values, xLab, yLab, title, names));
                } else {
                    aritLanguage.addSemanticError("Error : los tipos de los parametros no son los correctos " +
                            "para esta funcion `barplot()`.", info);
                }
            } else {
                aritLanguage.addSemanticError("Error : los tipos de los parametros no son los correctos " +
                        "para esta funcion `barplot()`.", info);
            }
        } else {
            aritLanguage.addSemanticError("Error : el tamaño `" + numberOfArguments +
                    "` de parámetros no es válido para la función barplot().", info);
        }
        return null;
    }

    @NotNull
    private BarChart<String, Number> getChart(double[] values, String xLab, String yLab, String title, String[] names) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xLab);
        xAxis.setStyle("-fx-font-size: 16px;");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLab);
        yAxis.setStyle("-fx-font-size: 16px;");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
        barChart.setStyle("-fx-background-color: white; -fx-font-weight: bold");
        barChart.getData().add(getSeries(values, names));
        barChart.setMinSize(600, 600);
        barChart.setMaxSize(1000, 1000);
        return barChart;
    }

    @NotNull
    private XYChart.Series<String, Number> getSeries(@NotNull double[] values, @NotNull String[] names) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Datos");
        int i, length = values.length;
        for (i = 0; i < length; i++) {
            series.getData().add(new XYChart.Data<>(names[i] + " | " + values[i], values[i]));
        }
        return series;
    }

    private double toDouble(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Double) return (Double) value;
        return 0;
    }

    private String toString(Object value) {
        return value != null ? value.toString() : "NULL";
    }

    private boolean isVector(@NotNull Object value) {
        return value instanceof AritVector;
    }

    private boolean isNumber(@NotNull AritVector vector) {
        return TYPE_FACADE.isIntegerType(vector.baseType) || TYPE_FACADE.isNumericType(vector.baseType);
    }

    private boolean isString(@NotNull AritVector vector) {
        return TYPE_FACADE.isStringType(vector.baseType);
    }

    public static BarPlotFunction getInstance() {
        return INSTANCE;
    }

    private static final BarPlotFunction INSTANCE = new BarPlotFunction();

    @Override
    public String toString() {
        return super.toString();
    }
}
