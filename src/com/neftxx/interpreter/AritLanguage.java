package com.neftxx.interpreter;

import com.neftxx.interpreter.ast.AstNode;
import com.neftxx.interpreter.ast.error.*;
import com.neftxx.interpreter.ast.scope.FileScope;
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
    public final TextArea console;
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

    public ArrayList<AstNode> getAstNodes() {
        return this.astNodes;
    }

    public void preInterpret() {

    }

    public void interpret() {
        if (this.astNodes != null) {
            for (AstNode node : astNodes) {
                node.interpret(this, globalScope);
            }
        }
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
