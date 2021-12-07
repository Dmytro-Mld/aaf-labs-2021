package dev.dmitriy.input;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Lexer {

    public static List<String> analyze(String input) {
        List<String> tokens = new ArrayList<>();

        String sql = input.replace("\n", "")
                .replace("\t", "")
                .replace("\r", "");


        StringTokenizer stringTokenizer =
                new StringTokenizer(sql, " (),;", true);

        while (stringTokenizer.hasMoreElements()) {
            tokens.add(stringTokenizer.nextToken());
        }
        tokens.removeIf(s -> s.equals(" "));

        return tokens;
    }

}
