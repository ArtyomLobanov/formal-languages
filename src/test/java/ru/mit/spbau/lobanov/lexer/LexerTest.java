package ru.mit.spbau.lobanov.lexer;

import org.junit.Test;
import ru.mit.spbau.lobanov.lexer.token.*;
import ru.mit.spbau.lobanov.lexer.token.KeyWordToken.KeyWordType;
import ru.mit.spbau.lobanov.lexer.token.OperatorToken.OperatorTokenType;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;
import static ru.mit.spbau.lobanov.lexer.LexerUtils.parse;
import static ru.mit.spbau.lobanov.lexer.token.KeyWordToken.KeyWordType.*;
import static ru.mit.spbau.lobanov.lexer.token.OperatorToken.OperatorTokenType.*;

public class LexerTest {
    @Test
    public void multilineCommentsTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/multiline_test.l").toURI()));
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(CommentToken.class::isInstance));
        assertArrayEquals(new String[]{"// // read;", "// lala", "// print pp"},
                result.stream()
                        .map(CommentToken.class::cast)
                        .map(CommentToken::getText)
                        .toArray());
    }

    @Test
    public void positioningTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/positioning_test.l").toURI()));
        assertEquals(6, result.size());
        assertArrayEquals(new int[]{0, 0, 0, 1, 1, 4},
                result.stream()
                        .mapToInt(Token::getLineIndex)
                        .toArray());
        assertArrayEquals(new int[]{0, 5, 9, 1, 7, 0},
                result.stream()
                        .mapToInt(Token::getStart)
                        .toArray());
        assertArrayEquals(new int[]{4, 6, 10, 6, 9, 1},
                result.stream()
                        .mapToInt(Token::getEnd)
                        .toArray());
    }

    @Test
    public void keyWordsTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/keywords_test.l").toURI()));
        assertEquals(9, result.size());
        assertTrue(result.stream().allMatch(KeyWordToken.class::isInstance));
        assertArrayEquals(new KeyWordType[]{IF, THEN, ELSE, WHILE, DO, READ, WRITE, BEGIN, END},
                result.stream()
                        .map(KeyWordToken.class::cast)
                        .map(KeyWordToken::getType)
                        .toArray());
    }

    @Test
    public void operatorsTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/operators_test.l").toURI()));
        assertEquals(16, result.size());
        assertTrue(result.stream().allMatch(OperatorToken.class::isInstance));
        assertArrayEquals(new OperatorTokenType[]{PLUS, MINUS, MULTIPLY, DIVIDE, MOD, EQUAL, NOT_EQUAL, GREATER,
                        GREATER_OR_EQUAL, LOWER, LOWER_OR_EQUAL, AND, OR, LEFT_BRACKET, RIGHT_BRACKET, SEMICOLON},
                result.stream()
                        .map(OperatorToken.class::cast)
                        .map(OperatorToken::getType)
                        .toArray());
    }

    @Test
    public void floatsTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/floats_test.l").toURI()));
        assertEquals(7, result.size());
        assertTrue(result.stream().allMatch(FloatToken.class::isInstance));
        assertArrayEquals(new Double[]{Double.valueOf("1233f"), Double.valueOf(".213"), Double.valueOf("0.E23"),
                        Double.valueOf("23E12"), Double.valueOf("0002.22"), Double.valueOf("0.23E-123"),
                        Double.valueOf("0.23e-123")},
                result.stream()
                        .map(FloatToken.class::cast)
                        .map(FloatToken::getValue)
                        .toArray());
    }

    @Test
    public void integerTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/integer_test.l").toURI()));
        assertEquals(5, result.size());
        assertTrue(result.get(0).getClass() == OperatorToken.class);
        assertTrue(result.stream().skip(1).allMatch(IntegerToken.class::isInstance));
        assertArrayEquals(new int[]{242345, 435, 234, 0},
                result.subList(1, 5).stream()
                        .map(IntegerToken.class::cast)
                        .map(IntegerToken::getValue)
                        .mapToInt(BigInteger::intValue)
                        .toArray());
    }

    @Test
    public void booleanTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/boolean_test.l").toURI()));
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(BooleanToken.class::isInstance));
        assertArrayEquals(new Boolean[]{true, false},
                result.stream()
                        .map(BooleanToken.class::cast)
                        .map(BooleanToken::getValue)
                        .toArray());
    }

    @Test
    public void identifierTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/identifier_test.l").toURI()));
        assertEquals(7, result.size());
        assertTrue(result.stream().allMatch(IdentifierToken.class::isInstance));
        assertArrayEquals(new String[]{"_ad", "sdf" , "f", "rgf", "_df11", "_11", "reredcc_"},
                result.stream()
                        .map(IdentifierToken.class::cast)
                        .map(IdentifierToken::getName)
                        .toArray());
    }

    @Test(expected = RuntimeException.class)
    public void wrongFloat() throws Exception {
        parse(Paths.get(LexerTest.class.getResource("/wrong_float_test.l").toURI()));
    }

    @Test
    public void simpleTest() throws Exception {
        final List<Token> result = parse(Paths.get(LexerTest.class.getResource("/example.l").toURI()));
        assertEquals(29, result.size());

        assertEquals(KeyWordToken.class, result.get(3).getClass());
        assertEquals(KeyWordToken.class, result.get(10).getClass());
        assertEquals(FloatToken.class, result.get(18).getClass());
        assertEquals(BooleanToken.class, result.get(25).getClass());

        assertEquals(23, ((IntegerToken) result.get(21)).getValue().intValue());

        assertEquals(3, result.get(27).getLineIndex());
        assertEquals(20, result.get(24).getStart());
        assertEquals(8, result.get(6).getEnd());
    }
}