package com.neftxx.interpreter;

import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.error.*;
import com.neftxx.interpreter.ast.expression.structure.AritStructure;
import com.neftxx.interpreter.ast.expression.structure.StructureNode;
import com.neftxx.interpreter.ast.scope.FileScope;
import com.neftxx.interpreter.ast.expression.function.Function;
import com.neftxx.interpreter.ast.scope.SymbolNode;
import com.neftxx.interpreter.javacc.Grammar;
import com.neftxx.interpreter.javacc.ParseException;
import com.neftxx.interpreter.jflex_cup.Lexer;
import com.neftxx.interpreter.jflex_cup.Parser;
import com.neftxx.util.NodeInfo;
import java_cup.runtime.ComplexSymbolFactory;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

public class AritLanguage {
    private ArrayList<AstNode> astNodes;
    public final ArrayList<NodeError> errors;
    public final String filename;
    private final TextArea console;
    private final VBox paneCharts;
    public final FileScope globalScope;

    public AritLanguage(String filename, TextArea console, VBox paneCharts) {
        this.errors = new ArrayList<>();
        this.astNodes = null;
        this.filename = filename;
        this.console = console;
        this.paneCharts = paneCharts;
        this.globalScope = new FileScope();
    }

    public void setAstNodes(ArrayList<AstNode> astNodes) {
        this.astNodes = astNodes;
    }

    public void saveFunctions() {
        if (this.astNodes != null) {
            for (AstNode node : astNodes) {
                if (node instanceof Function) {
                    Function function = (Function) node;
                    if (!function.verifyNamesOfParameters()) {
                        this.addSemanticError("Error : la función `" + function.id +
                                        "` tiene parámetros con el mismo nombre.", function.info);
                        continue;
                    }
                    if (!this.globalScope.addMethod(function.id, function)) {
                        this.addSemanticError("Error : la función `" + function.id + "` ya existe.",
                                function.info);
                    }
                }
            }
        }
    }

    public void interpret() {
        if (this.astNodes != null) {
            for (AstNode node : astNodes) {
                if (!(node instanceof Function)) node.interpret(this, globalScope);
            }
        }
    }

    public ArrayList<SymbolNode> getSymbols() {
        ArrayList<SymbolNode> symbols = new ArrayList<>();
        this.globalScope.getMethods().forEach((key, function) -> {
            symbols.add(new SymbolNode(
                    key, "Function", 0, "Global", function.info.line, function.referenceLines.toString()
            ));
            if (function.parameters != null) {
                function.parameters.forEach(parameter -> {
                    symbols.add(new SymbolNode(parameter.id, "Parametro", 0, key, function.info.line, ""));
                });
            }
        });
        this.globalScope.getVariables().forEach((key, variable) -> {
            int size = 0;
            if (variable.value instanceof AritStructure) {
                size = ((AritStructure) variable.value).size();
            }
            symbols.add(new SymbolNode(
                    key, variable.type.toString(), size, "Global",
                    variable.declarationLine, variable.referenceLines.toString()
            ));
        });
        return symbols;
    }

    public String createAstGraph() {
        StringBuilder astGraph = new StringBuilder();
        astGraph.append("digraph astGraph {\n")
                .append("rankdir=TB;\n")
                .append("node [shape=record, style=filled, fillcolor=seashell2];\n")
                .append("node").append(this.hashCode()).append("[label = \"AST\"];\n");
        if (this.astNodes != null) {
            for (AstNode node: astNodes) {
                node.createAstGraph(astGraph);
                astGraph.append("node").append(this.hashCode()).append(" -> ").append("node").append(node.hashCode())
                        .append(";\n");
            }
        }
        astGraph.append("}\n");
        return astGraph.toString();
    }

    public void addLexicalError(String description, NodeInfo nodeInfo) {
        errors.add(new NodeError(TypeError.LEXICAL, description, nodeInfo));
    }

    public void addSyntacticError(String description, NodeInfo nodeInfo) {
        errors.add(new NodeError(TypeError.SYNTACTIC, description, nodeInfo));
    }

    public void addSemanticError(String description, NodeInfo nodeInfo) {
        errors.add(new NodeError(TypeError.SEMANTIC, description, nodeInfo));
    }

    public void printOnConsole(String text) {
        this.console.setText(this.console.getText() + text + "\n");
    }

    public void addChart(Node node) {
        this.paneCharts.getChildren().add(node);
    }

    public void analyzeWithJflexAndCup(String text) throws Exception {
        StringReader sr = new StringReader(text);
        BufferedReader reader = new BufferedReader(sr);
        ComplexSymbolFactory sf = new ComplexSymbolFactory();
        Lexer lexer = new Lexer(reader, sf, this);
        Parser parse = new Parser(lexer, sf, this);
        parse.parse();
    }

    public void analyzeWithJavaCC(String text) {
        StringReader sr = new StringReader(text);
        BufferedReader reader = new BufferedReader(sr);
        Grammar parser = new Grammar(reader, this);
        try {
            parser.compilation_unit();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }
}
