package com.neftxx.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private static final FileChooser.ExtensionFilter R_FILES =
            new FileChooser.ExtensionFilter("R Files", "*.r", "*.arit", "*.R", "*.ARIT");
    private static final FileChooser.ExtensionFilter ALL_FILES =
            new FileChooser.ExtensionFilter("All Files", "*.*");

    @Nullable
    public static File createNewFile(String filePath) {
        try {
            File newFolder = new File(filePath);
            boolean ok = newFolder.createNewFile();
            if (!ok) {
                String errorMessage = "No se puede crear un nuevo archivo en esta ruta. Verifique que no exista.";
                DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, errorMessage);
            }
            return newFolder;
        } catch (IOException e) {
            String errorMessage = "No se puede crear un nuevo archivo en esta ruta.";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, e.getMessage(), errorMessage);
            return null;
        }
    }

    public static File openSourceFile(String title) {
        FileChooser fileChooser = getNewFileChooser(title);
        return fileChooser.showOpenDialog(null);
    }

    public static File createNewFileAndSave(String title) {
        FileChooser fileChooser = getNewFileChooser(title);
        return fileChooser.showSaveDialog(null);
    }

    @NotNull
    private static FileChooser getNewFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(R_FILES);
        fileChooser.getExtensionFilters().add(ALL_FILES);
        return fileChooser;
    }

    public static boolean updateContent(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException iox) {
            String errorMessage = "No se puede guardar este archivo, asegúrese de que este archivo no se elimine";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG,iox.getMessage(), errorMessage);
            return false;
        }
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        file.deleteOnExit();
    }

    public static File openSourceDir(String title) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        return chooser.showDialog(null);
    }

    @Nullable
    public static String readFile(File file) {
        try {
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException iox) {
            String errorMessage = "No se puede leer este archivo, asegúrese de que este archivo exista.";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, iox.getMessage(), errorMessage);
            return null;
        }
    }
}
