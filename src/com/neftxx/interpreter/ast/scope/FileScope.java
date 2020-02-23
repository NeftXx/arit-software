package com.neftxx.interpreter.ast.scope;

import com.neftxx.interpreter.ast.expression.function.Function;
import com.neftxx.interpreter.ast.statement.native_function.structure.ConcatenateFunction;
import com.neftxx.interpreter.ast.statement.native_function.PrintFunction;
import com.neftxx.interpreter.ast.statement.native_function.NativeFunction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class FileScope extends Scope {
    private HashMap<String, Function> methods;

    public FileScope() {
        super(null);
        this.methods = new HashMap<>();
    }

    public boolean addMethod(String id, Function function) {
        if (NATIVE_FUNCTIONS.containsKey(id) || this.methods.containsKey(id)) return false;
        this.methods.put(id, function);
        return true;
    }

    public Function getMethod(String id) {
        return methods.get(id);
    }

    public NativeFunction getNativeFunction(String id) {
        return NATIVE_FUNCTIONS.get(id);
    }

    @NotNull
    private static HashMap<String, NativeFunction> createNativeMethods() {
        HashMap<String, NativeFunction> methods = new HashMap<>();
        methods.put("c", ConcatenateFunction.getInstance());
        methods.put("print", PrintFunction.getInstance());
        return methods;
    }

    private static final HashMap<String, NativeFunction> NATIVE_FUNCTIONS = createNativeMethods();

    @Override
    public String toString() {
        return "FileScope {" +
                "methods=" + methods +
                ", previous=" + previous +
                ", variables=" + variables +
                " }";
    }
}
