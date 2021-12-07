package dev.dmitriy.input;

import java.util.regex.Pattern;

public enum TokenType {

    //remember about case insensitivity
    KEYWORD(Pattern.compile("SELECT|CREATE|INSERT|DELETE|INDEXED|INTO|FROM|WHERE|OR|AND" +
            "|select|create|insert|delete|indexed|into|from|where|or|and|\\*")),
    IDENTIFIER(Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*")),
    VALUE(Pattern.compile("\"(.*?)\"")),
    PUNCTUATION(Pattern.compile("[(),;]")),
    OPERATOR(Pattern.compile("<|>|<=|>=|=|!="));


    private final Pattern regex;

    TokenType(Pattern regex) {
        this.regex = regex;
    }

    public Boolean matches(String token){
        return regex.matcher(token).matches();
        //this.value = token if true
    }

    public static TokenType checkType(String token) {

        for (TokenType type : TokenType.values()) {
            if (type.matches(token)) {
                return type;
            }
        }

        return null;
    }

}
