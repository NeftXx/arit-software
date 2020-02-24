package com.neftxx.controller;

import com.neftxx.constant.Icons;
import com.neftxx.interpreter.AritLanguage;
import com.neftxx.interpreter.ast.error.NodeError;
import com.neftxx.util.DialogUtils;
import com.neftxx.util.FileManager;
import com.neftxx.util.ImageUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MainController implements Initializable {
    @FXML
    private MenuItem newFileMenuItem;
    @FXML
    private MenuItem openFileMenuItem;
    @FXML
    private MenuItem saveFileMenuItem;
    @FXML
    private MenuItem saveAsFileMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private RadioMenuItem jflexCupMenuItem;
    @FXML
    private RadioMenuItem javaCCMenuItem;
    @FXML
    private MenuItem consoleClearMenuItem;
    @FXML
    private MenuItem tableErrorsClearMenuItem;
    @FXML
    private MenuItem tableSymbolsClearMenuItem;
    @FXML
    private MenuItem allClearMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private Button newFileButton;
    @FXML
    private Button openFileButton;
    @FXML
    private Button saveFileButton;
    @FXML
    private Button saveAsFileButton;
    @FXML
    private Button startButton;
    @FXML
    private TabPane codeAreaLayout;
    @FXML
    private TabPane tabReportPane;
    @FXML
    private TextArea consoleTextArea;
    @FXML
    private TableView<NodeError> tableErrors;
    @FXML
    private TableColumn<String, NodeError> columnTypeError;
    @FXML
    private TableColumn<String, NodeError> columnDescription;
    @FXML
    private TableColumn<String, NodeError> columnLine;
    @FXML
    private TableColumn<String, NodeError> columnColumn;
    @FXML
    private TableColumn<String, NodeError> columnFile;
    @FXML
    private TableView tableSymbols;
    @FXML
    private TableColumn columnNameSym;
    @FXML
    private TableColumn columnTypeSym;
    @FXML
    private TableColumn columnScopeSym;
    @FXML
    private TableColumn columnRolSym;
    @FXML
    private TableColumn columnParameterSym;
    @FXML
    private TableColumn columnConstantSym;

    private ExecutorService executorService;
    private static Logger debugger;
    private static final String DEBUG_TAG = MainController.class.getSimpleName();
    private static final int THREAD_AVAILABLE_NUMBER = Runtime.getRuntime().availableProcessors();
    private static final int TABLE_ERRORS_TAB = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        executorService = Executors.newFixedThreadPool(THREAD_AVAILABLE_NUMBER);
        debugger = Logger.getLogger(DEBUG_TAG);
        codeAreaLayout.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        initTableErrors();
        onMenuItemsActions();
    }

    private void initTableErrors() {
        tableErrors.setPlaceholder(new Label("No hay errores que mostrar."));
        columnTypeError.setCellValueFactory(new PropertyValueFactory<>("typeError"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnLine.setCellValueFactory(new PropertyValueFactory<>("line"));
        columnColumn.setCellValueFactory(new PropertyValueFactory<>("column"));
        columnFile.setCellValueFactory(new PropertyValueFactory<>("filename"));
    }

    private void onMenuItemsActions() {
        aboutMenuItem.setOnAction(actionEvent ->
                DialogUtils.createInformationDialog(
                        "Sobre el proyecto", "Autor: Ronald Berdúo", "Carnet: 201504420"
                )
        );
        newFileMenuItem.setOnAction(event -> onNewFileAction());
        newFileButton.setOnAction(event -> onNewFileAction());
        openFileMenuItem.setOnAction(event -> onOpenFileAction());
        openFileButton.setOnAction(event -> onOpenFileAction());
        saveFileMenuItem.setOnAction(event -> onSaveFileAction());
        saveFileButton.setOnAction(event -> onSaveFileAction());
        saveAsFileMenuItem.setOnAction(event -> onSaveAsFileAction());
        saveAsFileButton.setOnAction(event -> onSaveAsFileAction());
        startButton.setOnAction(event -> onRunFileAction());
        consoleClearMenuItem.setOnAction(event -> consoleTextArea.clear());
        tableErrorsClearMenuItem.setOnAction(event -> tableErrors.getItems().clear());
        tableSymbolsClearMenuItem.setOnAction(event -> tableSymbols.getItems().clear());
        allClearMenuItem.setOnAction(event -> clearElements());
        exitMenuItem.setOnAction(event -> onExitAction());
    }

    private void onNewFileAction() {
        File newFile = FileManager.createNewFileAndSave("Nuevo archivo");
        if (newFile != null) {
            try {
                if (newFile.createNewFile()) {
                    openSourceInTab(newFile);
                }
            } catch (IOException iox) {
                String warnMessage = "No se pudo crear el archivo";
                debugger.warning(warnMessage);
                DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, warnMessage, "");
            }
        }
    }

    /**
     * Muestra un FileChooser para seleccionar un archivo, para poder abrirlo
     */
    private void onOpenFileAction() {
        File outputFile = FileManager.openSourceFile("Abrir archivo Arit");
        if (outputFile != null)
            executorService.execute(() -> openSourceInTab(outputFile));
    }

    /**
     * Guarda el archivo del tab actual
     */
    private void onSaveFileAction() {
        if (!codeAreaLayout.getTabs().isEmpty()) {
            Tab currentTab = codeAreaLayout.getSelectionModel().getSelectedItem();
            VirtualizedScrollPane<?> pane = (VirtualizedScrollPane<?>) currentTab.getContent();
            CodeArea codeArea = (CodeArea) pane.getContent();
            String text = codeArea.getText();
            String filename = currentTab.getId();
            if (filename.isEmpty()) {
                createNewFileAndSave(currentTab, text);
            } else {
                File file = new File(filename);
                if (FileManager.updateContent(file, text)) {
                    DialogUtils.createInformationDialog("Guardar archivo", "Archivo guardado.",
                            "Se guardo el archivo " + currentTab.getText() + " con éxito.");
                }
            }
        }
    }

    private void onSaveAsFileAction() {
        if (!codeAreaLayout.getTabs().isEmpty()) {
            Tab currentTab = codeAreaLayout.getSelectionModel().getSelectedItem();
            VirtualizedScrollPane<?> pane = (VirtualizedScrollPane<?>) currentTab.getContent();
            CodeArea codeArea = (CodeArea) pane.getContent();
            String text = codeArea.getText();
            createNewFileAndSave(currentTab, text);
        }
    }

    private void createNewFileAndSave(Tab currentTab, String text) {
        File newFile = FileManager.createNewFileAndSave("Guardar como");
        if (newFile != null) {
            try {
                if (newFile.createNewFile()) {
                    currentTab.setText(newFile.getName());
                    currentTab.setId(newFile.getAbsolutePath());
                    currentTab.setUserData(newFile.getPath());
                    if (FileManager.updateContent(newFile, text)) {
                        DialogUtils.createInformationDialog("Guardar archivo", "Archivo guardado.",
                                "Se guardo el archivo " + currentTab.getText() + " con éxito.");
                    }
                }
            } catch (IOException e) {
                String warnMessage = "No se pudo crear el archivo";
                debugger.warning(warnMessage);
                DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, warnMessage, "");
            }
        }
    }

    private void onRunFileAction() {
        if (!codeAreaLayout.getTabs().isEmpty()) {
            clearElements();
            Tab currentTab = codeAreaLayout.getSelectionModel().getSelectedItem();
            VirtualizedScrollPane<?> pane = (VirtualizedScrollPane<?>) currentTab.getContent();
            CodeArea codeArea = (CodeArea) pane.getContent();
            String text = codeArea.getText();
            AritLanguage aritLanguage = new AritLanguage(currentTab.getText(), consoleTextArea);
            try {
                if (jflexCupMenuItem.isSelected())aritLanguage.analyzeWithJflexAndCup(text);
                else if (javaCCMenuItem.isSelected()) aritLanguage.analyzeWithJavaCC(text);
                else aritLanguage.analyzeWithJflexAndCup(text);
                aritLanguage.saveFunctions();
                aritLanguage.interpret();
                if (!aritLanguage.errors.isEmpty()) {
                    showErrorsInTableView(aritLanguage.errors);
                }
            } catch (Exception e) {
                String warnMessage = "Error al analizar el archivo " + currentTab.getText();
                debugger.warning(warnMessage);
                DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG, warnMessage, "");
            }
        }
    }

    /**
     * Lee un archivo RMB y lo abre en un CodeArea
     *
     * @param sourceFile archivo
     */
    private void openSourceInTab(File sourceFile) {
        if (Objects.nonNull(sourceFile)) {
            boolean exists = selectTabIfItExists(sourceFile.getAbsolutePath());
            if (!exists) {
                Tab rmbTab = new Tab(sourceFile.getName());
                rmbTab.setId(sourceFile.getAbsolutePath());
                rmbTab.setUserData(sourceFile.getPath());
                rmbTab.setOnClosed(event -> onTabCloseAction(rmbTab));
                rmbTab.setGraphic(ImageUtils.buildImageView(Icons.codeIconImage));
                String code = FileManager.readFile(sourceFile);
                if (code != null) {
                    CodeArea codeTextArea = new CodeArea();
                    EditorController editorController = new EditorController(codeTextArea);
                    editorController.editorSettings();
                    codeTextArea.replaceText(0, 0, code);
                    rmbTab.setContent(new VirtualizedScrollPane<>(codeTextArea));
                    Platform.runLater(() -> {
                        codeAreaLayout.getTabs().add(rmbTab);
                        codeAreaLayout.getSelectionModel().select(rmbTab);
                    });
                }
            }
        }
    }

    /**
     * Verifica si un archivo ya esta abierto en el editor
     *
     * @param absolutePath ruta
     * @return boolean
     */
    private boolean selectTabIfItExists(String absolutePath) {
        ObservableList<Tab> tabs = codeAreaLayout.getTabs();
        for (Tab tab: tabs) {
            if (tab.getId().equals(absolutePath)) {
                SingleSelectionModel<Tab> selectionModel = codeAreaLayout.getSelectionModel();
                selectionModel.select(tab);
                return true;
            }
        }
        return false;
    }

    /**
     * Limpia el codeArea cuando se cierra el tab
     *
     * @param tab Tab
     */
    private void onTabCloseAction(Tab tab) {
        if (Objects.nonNull(tab)) {
            CodeArea codeArea = (CodeArea) ((Parent) tab.getContent()).getChildrenUnmodifiable().get(0);
            codeArea.displaceCaret(0);
        }
    }

    /**
     * Muestra los errores en la tabla
     */
    private void showErrorsInTableView(@NotNull ArrayList<NodeError> errors) {
        tableErrors.getItems().clear();
        tableErrors.setItems(FXCollections.observableArrayList(errors));
        tabReportPane.getSelectionModel().select(TABLE_ERRORS_TAB);
    }

    /**
     * para cerrar la aplicacion
     */
    private void onExitAction() {
        Platform.exit();
        System.exit(0);
    }

    private void clearElements() {
        this.consoleTextArea.clear();
        this.tableErrors.getItems().clear();
        this.tableSymbols.getItems().clear();
    }
}
