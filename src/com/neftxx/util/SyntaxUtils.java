package com.neftxx.util;

import java.util.regex.Pattern;

public class SyntaxUtils {
    public static final String[] KEYWORDS = new String[]{
            "continue", "function", "default", "return", "switch", "break", "while", "case", "else", "for", "in", "do", "if"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "[()]";
    private static final String BRACE_PATTERN = "[{}]";
    private static final String BRACKET_PATTERN = "[\\[\\]]";
    private static final String SEMICOLON_PATTERN = ";";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "#\\*(.|\\R)*?\\*#"+ "|" + "#[^\n]*";
    private static final String OPERATION_PATTERN = "=>|==|!=|>=|<=|%%|\\+|-|\\*|/|\\^|=|>|<|\\?|:|[|]|&|!|,";
    private static final String NUMBERS_PATTERN = "[0-9]+(\".\"[0-9]+)?";

    public static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<OPERATION>" + OPERATION_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBERS_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );
}
