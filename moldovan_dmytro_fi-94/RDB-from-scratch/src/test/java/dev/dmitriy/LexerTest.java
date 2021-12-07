package dev.dmitriy;

import dev.dmitriy.input.Lexer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class LexerTest {

    @Test
    public void test0() {
        final String input = "CREATE cats (id INDEXED, name INDEXED, favourite_food);";
        final List<String> expected = List.of("CREATE", "cats", "(", "id", "INDEXED",
                ",", "name", "INDEXED",",", "favourite_food", ")", ";");

        Assert.assertEquals(expected, Lexer.analyze(input));
    }

    @Test
    public void test1() {
        final String input = "\tCREATE   cats \n(id INDEXED, \tname INDEXED, \rfavourite_food);";
        final List<String> expected = List.of("CREATE", "cats", "(", "id", "INDEXED", ",",
                "name", "INDEXED", ",", "favourite_food", ")", ";");

        Assert.assertEquals(expected, Lexer.analyze(input));
    }

    @Test
    public void test2() {
        final String input = "INSERT INTO cats (\"1\", \"Murzik\", \"Sausages\");";
        final List<String> expected = List.of("INSERT", "INTO", "cats", "(", "\"1\"", ",",
                "\"Murzik\"", ",", "\"Sausages\"", ")", ";");

        Assert.assertEquals(expected, Lexer.analyze(input));
    }

    @Test
    public void test3() {
        final String input = "SELECT * FROM cats;";
        final List<String> expected = List.of("SELECT", "*", "FROM", "cats", ";");

        Assert.assertEquals(expected, Lexer.analyze(input));
    }

    @Test
    public void test4() {
        final String input = "SELECT id, favourite_food \n" +
                "  FROM cats \n" +
                "  WHERE (name <= \"Murzik\") OR (name = \"Pushok\");";
        final List<String> expected = List.of("SELECT", "id", ",", "favourite_food", "FROM",
                "cats", "WHERE", "(", "name", "<=", "\"Murzik\"", ")", "OR", "(", "name", "=",
                "\"Pushok\"", ")", ";");

        Assert.assertEquals(expected, Lexer.analyze(input));
    }

    @Test
    public void test5() {
        final String input = "DELETE FROM cats;";
        final List<String> expected = List.of("DELETE", "FROM", "cats", ";");

        Assert.assertEquals(expected, Lexer.analyze(input));
    }

    @Test
    public void test6() {
        final String input = "DELETE cats WHERE name = \"Murzik\";";
        final List<String> expected = List.of("DELETE", "cats", "WHERE", "name", "=",
                "\"Murzik\"", ";");

        Assert.assertEquals(expected, Lexer.analyze(input));
    }

    @Test
    public void test7() {
        final String input = "DELETE cats WHERE id != \"2\";";
        final List<String> expected = List.of("DELETE", "cats", "WHERE", "id", "!=", "\"2\"", ";");

        Assert.assertEquals(expected, Lexer.analyze(input));
    }


}
