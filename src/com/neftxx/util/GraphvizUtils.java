package com.neftxx.util;

import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.io.PrintWriter;

public class GraphvizUtils {
    @Nullable
    public static void createGraph(String code) {
        FileWriter fileWriter = null;
        PrintWriter printWriter;
        String errorMessage;
        try {
            fileWriter = new FileWriter("graph.dot");
            printWriter = new PrintWriter(fileWriter);
            printWriter.print(code);
        }
        catch (Exception e) {
            errorMessage = "Error al escribir el archivo graph.dot";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, e.getMessage(), errorMessage);
        } finally {
            try {
                if (fileWriter != null) fileWriter.close();
            } catch (Exception e2){
                errorMessage = "Error al cerrar el archivo graph.dot";
                DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, e2.getMessage(), errorMessage);
            }
        }
        try {
            Process process = Runtime.getRuntime().exec( "dot -Tpng -o graph.png graph.dot");
            process.waitFor();
            String [] commands = {
                    "cmd.exe" , "/c", "start" , "\"DummyTitle\"", "\"graph.png\""
            };
            process = Runtime.getRuntime().exec(commands);
            process.waitFor();
        } catch (Exception ex) {
            errorMessage = "Error al generar la imagen para el archivo graph.dot";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, ex.getMessage(), errorMessage);
        }
    }
}
