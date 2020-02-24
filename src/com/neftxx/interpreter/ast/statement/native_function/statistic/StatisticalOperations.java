package com.neftxx.interpreter.ast.statement.native_function.statistic;

import org.jetbrains.annotations.NotNull;

public class StatisticalOperations {
    public static double calculateMean(@NotNull double[] values) {
        double prom = 0.0;
        int size = values.length;
        for (double value : values) prom += value;
        return prom / (double) size;
    }

    public static double calculateMedian(@NotNull double[] values) {
        int pos = 0, size = values.length;
        double temp = 0, temp0 = 0;
        quickSort(values, 0, size - 1);
        temp = size / 2;
        if (size % 2 == 0 ) {
            pos = (int) temp;
            temp0 = (double)(values[pos] / values[pos + 1]);
        }
        if ( size % 2 == 1 ) {
            pos = (int)(temp + 0.5);
            temp0 = (double)(values[ pos ]);
        }

        return temp0;
    }

    public static double calculateMode(@NotNull double[] values) {
        int i, j, size = values.length;
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

    private static void quickSort(@NotNull double[] values, int left, int right){
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
}
