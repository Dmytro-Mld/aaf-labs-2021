package dev.dmitriy;

import dev.dmitriy.input.Lexer;
import dev.dmitriy.input.SyntaxAnalyzer;
import dev.dmitriy.input.TokenType;
import dev.dmitriy.input.TypeValuePair;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class SyntaxAnalyzerTest {

    @Test
    public void test1() {
        final String input = "CREATE cats (id INDEXED, name INDEXED, favourite_food);";
        List<TypeValuePair> actualTokens = SyntaxAnalyzer.verify(Lexer.analyze(input));
        List<TypeValuePair> expectedTokens = List.of(
                new TypeValuePair(TokenType.KEYWORD, "CREATE"),
                new TypeValuePair(TokenType.IDENTIFIER, "cats"),
                new TypeValuePair(TokenType.PUNCTUATION, "("),
                new TypeValuePair(TokenType.IDENTIFIER, "id"),
                new TypeValuePair(TokenType.KEYWORD, "INDEXED"),
                new TypeValuePair(TokenType.PUNCTUATION, ","),
                new TypeValuePair(TokenType.IDENTIFIER, "name"),
                new TypeValuePair(TokenType.KEYWORD, "INDEXED"),
                new TypeValuePair(TokenType.PUNCTUATION, ","),
                new TypeValuePair(TokenType.IDENTIFIER, "favourite_food"),
                new TypeValuePair(TokenType.PUNCTUATION, ")"),
                new TypeValuePair(TokenType.PUNCTUATION, ";")
                );

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void test2() {
        final String input = "INSERT INTO cats (\"1\", \"Murzik\", \"Sausages\");";
        List<TypeValuePair> actualTokens = SyntaxAnalyzer.verify(Lexer.analyze(input));
        List<TypeValuePair> expectedTokens = List.of(
                new TypeValuePair(TokenType.KEYWORD, "INSERT"),
                new TypeValuePair(TokenType.KEYWORD, "INTO"),
                new TypeValuePair(TokenType.IDENTIFIER, "cats"),
                new TypeValuePair(TokenType.PUNCTUATION, "("),
                new TypeValuePair(TokenType.VALUE, "\"1\""),
                new TypeValuePair(TokenType.PUNCTUATION, ","),
                new TypeValuePair(TokenType.VALUE, "\"Murzik\""),
                new TypeValuePair(TokenType.PUNCTUATION, ","),
                new TypeValuePair(TokenType.VALUE, "\"Sausages\""),
                new TypeValuePair(TokenType.PUNCTUATION, ")"),
                new TypeValuePair(TokenType.PUNCTUATION, ";")
        );

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void test3() {
        final String input = "SELECT * FROM cats;";
        List<TypeValuePair> actualTokens = SyntaxAnalyzer.verify(Lexer.analyze(input));
        List<TypeValuePair> expectedTokens = List.of(
                new TypeValuePair(TokenType.KEYWORD, "SELECT"),
                new TypeValuePair(TokenType.KEYWORD, "*"),
                new TypeValuePair(TokenType.KEYWORD, "FROM"),
                new TypeValuePair(TokenType.IDENTIFIER, "cats"),
                new TypeValuePair(TokenType.PUNCTUATION, ";")
        );

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void test4() {
        final String input = "SELECT id, favourite_food \n" +
                "  FROM cats \n" +
                "  WHERE (name <= \"Murzik\") OR (name = \"Pushok\");";
        List<TypeValuePair> actualTokens = SyntaxAnalyzer.verify(Lexer.analyze(input));
        List<TypeValuePair> expectedTokens = List.of(
                new TypeValuePair(TokenType.KEYWORD, "SELECT"),
                new TypeValuePair(TokenType.IDENTIFIER, "id"),
                new TypeValuePair(TokenType.PUNCTUATION, ","),
                new TypeValuePair(TokenType.IDENTIFIER, "favourite_food"),
                new TypeValuePair(TokenType.KEYWORD, "FROM"),
                new TypeValuePair(TokenType.IDENTIFIER, "cats"),
                new TypeValuePair(TokenType.KEYWORD, "WHERE"),
                new TypeValuePair(TokenType.PUNCTUATION, "("),
                new TypeValuePair(TokenType.IDENTIFIER, "name"),
                new TypeValuePair(TokenType.OPERATOR, "<="),
                new TypeValuePair(TokenType.VALUE, "\"Murzik\""),
                new TypeValuePair(TokenType.PUNCTUATION, ")"),
                new TypeValuePair(TokenType.KEYWORD, "OR"),
                new TypeValuePair(TokenType.PUNCTUATION, "("),
                new TypeValuePair(TokenType.IDENTIFIER, "name"),
                new TypeValuePair(TokenType.OPERATOR, "="),
                new TypeValuePair(TokenType.VALUE, "\"Pushok\""),
                new TypeValuePair(TokenType.PUNCTUATION, ")"),
                new TypeValuePair(TokenType.PUNCTUATION, ";")
        );

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void test5() {
        final String input = "DELETE FROM cats;";
        List<TypeValuePair> actualTokens = SyntaxAnalyzer.verify(Lexer.analyze(input));
        List<TypeValuePair> expectedTokens = List.of(
                new TypeValuePair(TokenType.KEYWORD, "DELETE"),
                new TypeValuePair(TokenType.KEYWORD, "FROM"),
                new TypeValuePair(TokenType.IDENTIFIER, "cats"),
                new TypeValuePair(TokenType.PUNCTUATION, ";")
        );

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void test6() {
        final String input = "DELETE cats WHERE name = \"Murzik\";";
        List<TypeValuePair> actualTokens = SyntaxAnalyzer.verify(Lexer.analyze(input));
        List<TypeValuePair> expectedTokens = List.of(
                new TypeValuePair(TokenType.KEYWORD, "DELETE"),
                new TypeValuePair(TokenType.IDENTIFIER, "cats"),
                new TypeValuePair(TokenType.KEYWORD, "WHERE"),
                new TypeValuePair(TokenType.IDENTIFIER, "name"),
                new TypeValuePair(TokenType.OPERATOR, "="),
                new TypeValuePair(TokenType.VALUE, "\"Murzik\""),
                new TypeValuePair(TokenType.PUNCTUATION, ";")
        );

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void test7() {
        final String input = "DELETE cats WHERE id != \"2\";";
        List<TypeValuePair> actualTokens = SyntaxAnalyzer.verify(Lexer.analyze(input));
        List<TypeValuePair> expectedTokens = List.of(
                new TypeValuePair(TokenType.KEYWORD, "DELETE"),
                new TypeValuePair(TokenType.IDENTIFIER, "cats"),
                new TypeValuePair(TokenType.KEYWORD, "WHERE"),
                new TypeValuePair(TokenType.IDENTIFIER, "id"),
                new TypeValuePair(TokenType.OPERATOR, "!="),
                new TypeValuePair(TokenType.VALUE, "\"2\""),
                new TypeValuePair(TokenType.PUNCTUATION, ";")
        );

        Assert.assertEquals(expectedTokens, actualTokens);
    }
}
