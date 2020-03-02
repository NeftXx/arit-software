package com.neftxx.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class GraphvizUtils {
    @Nullable
    public static Image createGraph(String code) {
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
            Runtime rt = Runtime.getRuntime();
            rt.exec( "dot -Tpng -o graph.png graph.dot");
            Thread.sleep(1000);
            File imageFile = new File("graph.png");
            return SwingFXUtils.toFXImage(ImageIO.read(imageFile), null);
        } catch (Exception ex) {
            errorMessage = "Error al generar la imagen para el archivo graph.dot";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, ex.getMessage(), errorMessage);
        }
        return null;
    }
}
