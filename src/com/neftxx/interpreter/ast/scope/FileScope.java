package com.neftxx.interpreter.ast.scope;

import com.neftxx.interpreter.ast.expression.function.Function;
import com.neftxx.interpreter.ast.statement.native_function.*;
import com.neftxx.interpreter.ast.statement.native_function.graph.BarPlotFunction;
import com.neftxx.interpreter.ast.statement.native_function.graph.HistFunction;
import com.neftxx.interpreter.ast.statement.native_function.graph.PieFunction;
import com.neftxx.interpreter.ast.statement.native_function.graph.PlotFunction;
import com.neftxx.interpreter.ast.statement.native_function.statistic.MeanFunction;
import com.neftxx.interpreter.ast.statement.native_function.statistic.MedianFunction;
import com.neftxx.interpreter.ast.statement.native_function.statistic.ModeFunction;
import com.neftxx.interpreter.ast.statement.native_function.structure.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class FileScope extends Scope {
    private HashMap<String, Function> methods;

    public FileScope() {
        this.methods = new HashMap<>();
    }

    public boolean addMethod(String id, Function function) {
        if (NATIVE_FUNCTIONS.containsKey(id) || this.methods.containsKey(id)) return false;
        this.methods.put(id, function);
        return true;
    }

    public Function getMethod(String id, int numberOfParameters) {
        Function function = methods.get(id);
        if (function != null)
            return numberOfParameters == function.getNumberOfParameters() ? function : null;
        return null;
    }

    public NativeFunction getNativeFunction(String id) {
        return NATIVE_FUNCTIONS.get(id);
    }

    @NotNull
    private static HashMap<String, NativeFunction> createNativeMethods() {
        HashMap<String, NativeFunction> methods = new HashMap<>();
        methods.put("print", PrintFunction.getInstance());
        methods.put("plot", PlotFunction.getInstance());
        methods.put("hist", HistFunction.getInstance());
        methods.put("barplot", BarPlotFunction.getInstance());
        methods.put("pie", PieFunction.getInstance());
        methods.put("typeof", TypeofFunction.getInstance());
        methods.put("trunk", TrunkFunction.getInstance());
        methods.put("touppercase", ToUpperCaseFunction.getInstance());
        methods.put("tolowercase", ToLowerCaseFunction.getInstance());
        methods.put("stringlength", StringLengthFunction.getInstance());
        methods.put("round", RoundFunction.getInstance());
        methods.put("remove", RemoveFunction.getInstance());
        methods.put("nrow", NRowFunction.getInstance());
        methods.put("ncol", NColFunction.getInstance());
        methods.put("length", LengthFunction.getInstance());
        methods.put("c", ConcatenateFunction.getInstance());
        methods.put("list", ListFunction.getInstance());
        methods.put("array", ArrayFunction.getInstance());
        methods.put("matrix", MatrixFunction.getInstance());
        methods.put("mode", ModeFunction.getInstance());
        methods.put("median", MedianFunction.getInstance());
        methods.put("mean", MeanFunction.getInstance());
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
