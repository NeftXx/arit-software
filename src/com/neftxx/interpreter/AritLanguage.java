package com.neftxx.interpreter;

import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.error.*;
import com.neftxx.interpreter.ast.scope.FileScope;
import com.neftxx.interpreter.ast.expression.function.Function;
import com.neftxx.interpreter.jflex_cup.Lexer;
import com.neftxx.interpreter.jflex_cup.Parser;
import com.neftxx.util.NodeInfo;
import java_cup.runtime.ComplexSymbolFactory;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

public class AritLanguage {
    private ArrayList<AstNode> astNodes;
    public final ArrayList<NodeError> errors;
    public final String filename;
    private final TextArea console;
    public final FileScope globalScope;

    public AritLanguage(String filename, TextArea console) {
        this.errors = new ArrayList<>();
        this.astNodes = null;
        this.filename = filename;
        this.console = console;
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

    public String createAstGraph() {
        StringBuilder astGraph = new StringBuilder();
        astGraph.append("digraph astGraph {\n")
                .append("rankdir=TB;\n")
                .append("node [shape=record, style=filled, fillcolor=seashell2];\n");
        if (this.astNodes != null) {
            for (AstNode node: astNodes) {
                node.createAstGraph(astGraph);
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

    public void analyzeWithJflexAndCup(String text) throws Exception {
        StringReader sr = new StringReader(text);
        BufferedReader reader = new BufferedReader(sr);
        ComplexSymbolFactory sf = new ComplexSymbolFactory();
        Lexer lexer = new Lexer(reader, sf, this);
        Parser parse = new Parser(lexer, sf, this);
        parse.parse();
    }

    public void analyzeWithJavaCC(String text) { }
}
