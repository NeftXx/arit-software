package com.neftxx.interpreter.ast.statement.native_function.statistic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StatisticalOperations {
    public static double calculateMean(@NotNull double[] values, double trim) {
        double prom = 0.0;
        int size = 0;
        for (double value: values) {
            if (value > trim) {
                prom += value;
                size++;
            }
        }
        return size == 0 ? 0 : prom / (double) size;
    }

    public static double calculateMean(@NotNull double[] values) {
        double prom = 0.0;
        int size = values.length;
        for (double value : values) prom += value;
        return prom / (double) size;
    }

    public static double calculateMedian(@NotNull double[] values) {
        int size = values.length;
        if (size > 0) {
            int temp;
            double median;
            quickSort(values, 0, size - 1);
            temp = (size >> 1) - 1;
            if (size % 2 == 0) {
                median = (values[temp] + values[temp + 1]) / 2.0;
            } else {
                median = values[temp];
            }
            return median;
        }
        return 0;
    }

    public static double calculateMedian(@NotNull double[] values, double trim) {
        return calculateMedian(getValuesTrim(values, trim));
    }

    public static double calculateMode(@NotNull double[] values) {
        int size = values.length;
        if (size > 0) {
            int i, j;
            int frequencyTemp, frequencyMode = 0;
            double mode = -1;
            quickSort(values, 0, size - 1);
            for (i = 0; i < size; i++ ) {
                frequencyTemp = 1;
                for (j = i + 1; j < size; j++ ) {
                    if (values[i] == values[j] )
                        frequencyTemp++;
                }
                if (frequencyTemp > frequencyMode) {
                    frequencyMode = frequencyTemp;
                    mode = values[i];
                }
            }
            return mode;
        }
        return 0;
    }

    public static double calculateMode(@NotNull double[] values, double trim) {
        return calculateMode(getValuesTrim(values, trim));
    }

    public static void quickSort(@NotNull double[] values, int left, int right){
        double pivot = values[left];
        int i = left;
        int j = right;
        double aux;

        while (i < j) {
            while(values[i] <= pivot && i < j) i++;
            while(values[j] > pivot) j--;
            if (i < j) {
                aux = values[i];
                values[i] = values[j];
                values[j] = aux;
            }
        }

        values[left] = values[j];
        values[j] = pivot;
        if (left < j - 1) quickSort(values, left, j - 1);
        if (j + 1 < right) quickSort(values, j + 1, right);
    }

    @NotNull
    private static double[] getValuesTrim(@NotNull double[] values, double trim) {
        ArrayList<Integer> positions = new ArrayList<>();
        int i;
        int size = values.length;
        for(i = 0; i < size; i++) {
            if (values[i] > trim) {
                positions.add(i);
            }
        }
        size = positions.size();
        double[] elements = new double[positions.size()];
        for(i = 0; i < size; i++) {
            elements[i] = values[positions.get(i)];
        }
        return elements;
    }
}
