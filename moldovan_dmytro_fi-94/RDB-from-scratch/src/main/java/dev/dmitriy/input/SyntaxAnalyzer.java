package dev.dmitriy.input;


import dev.dmitriy.input.exceptions.IncorrectSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {

    public static List<TypeValuePair> verify(List<String> tokens) {
        List<TypeValuePair>  validTokens = new ArrayList<>();

        tokens.forEach(token -> {
            TokenType type = TokenType.checkType(token);

            if (type == null) {
                throw new IncorrectSyntaxException("Invalid value: " + token);
            }

            validTokens.add(new TypeValuePair(type, token));
        });


        return validTokens;
    }



}
